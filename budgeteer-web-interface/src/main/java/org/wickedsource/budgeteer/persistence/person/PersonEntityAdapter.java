package org.wickedsource.budgeteer.persistence.person;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.domain.PersonRate;
import de.adesso.budgeteer.core.person.domain.PersonWithRates;
import de.adesso.budgeteer.core.person.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonEntityAdapter implements
        GetPeopleInProjectPort,
        GetPersonByIdPort,
        GetPersonWithRatesByPersonIdPort,
        DeletePersonByIdPort,
        GetPeopleInBudgetPort,
        CreatePersonEntityPort,
        UpdatePersonEntityPort {
    private final PersonRepository personRepository;
    private final BudgetRepository budgetRepository;
    private final ProjectRepository projectRepository;
    private final WorkRecordRepository workRecordRepository;
    private final PersonMapper personMapper;

    @Override
    @Transactional
    public List<Person> getPeopleInProject(Long projectId) {
        return personMapper.mapToPerson(personRepository.findByProjectIdOrderByNameAsc(projectId));
    }

    @Override
    @Transactional
    public Optional<Person> getPersonById(Long personId) {
        return personRepository.findById(personId).map(personMapper::mapToPerson);
    }

    @Override
    @Transactional
    public PersonWithRates getPersonWithRatesByPersonId(Long personId) {
        return personMapper.mapToPersonWithRates(personRepository.findOneFetchDailyRates(personId));
    }

    @Override
    @Transactional
    public void deletePersonById(long personId) {
        personRepository.deleteById(personId);
    }

    @Override
    @Transactional
    public List<Person> getPeopleInBudget(long budgetId) {
        return personRepository.findByBudgetId(budgetId).stream().map(personMapper::mapToPerson).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Person createPersonEntity(CreatePersonEntityCommand command) {
        var personEntity = new PersonEntity();

        personEntity.setName(command.getPersonName());
        personEntity.setImportKey(command.getImportKey());
        personEntity.setDefaultDailyRate(command.getDefaultDailyRate());
        personEntity.setProject(projectRepository.findById(command.getProjectId()).orElseThrow());
        addDailyRates(personEntity, command.getRates());

        return personMapper.mapToPerson(personRepository.save(personEntity));
    }

    @Override
    @Transactional
    public Person updatePerson(UpdatePersonEntityCommand command) {
        var personEntity = personRepository.findById(command.getPersonId()).orElseThrow(RuntimeException::new);
        
        personEntity.setName(command.getPersonName());
        personEntity.setImportKey(personEntity.getImportKey());
        personEntity.setDefaultDailyRate(personEntity.getDefaultDailyRate());
        addDailyRates(personEntity, command.getRates());

        return personMapper.mapToPerson(personRepository.save(personEntity));
    }

    private void addDailyRates(PersonEntity personEntity, List<PersonRate> rates) {
        personEntity.getDailyRates().clear();

        rates.stream()
                .map(rate -> {
                    workRecordRepository.updateDailyRates(rate.getBudgetId(), personEntity.getId(), Date.valueOf(rate.getDateRange().getStartDate()), Date.valueOf(rate.getDateRange().getEndDate()), rate.getRate());

                    DailyRateEntity rateEntity = new DailyRateEntity();

                    rateEntity.setRate(rate.getRate());
                    rateEntity.setBudget(budgetRepository.findById(rate.getBudgetId()).orElse(null));
                    rateEntity.setPerson(personEntity);
                    rateEntity.setDateStart(Date.valueOf(rate.getDateRange().getStartDate()));
                    rateEntity.setDateEnd(Date.valueOf(rate.getDateRange().getStartDate()));

                    return rateEntity;
                })
                .forEach(rateEntity -> personEntity.getDailyRates().add(rateEntity));
    }
}
