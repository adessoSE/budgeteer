package org.wickedsource.budgeteer.service.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonBaseDataMapper personBaseDataMapper;

    @Autowired
    private PersonDetailDataMapper personDetailDataMapper;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private RecordRepository recordRepository;

    /**
     * Returns all people the given user can make use of to manage budgets.
     *
     * @param projectId ID of the project.
     * @return list of PersonBaseData objects for all people the given user can make use of to manage budgets.
     */
    public List<PersonBaseData> loadPeopleBaseData(long projectId) {
        return personBaseDataMapper.map(personRepository.findBaseDataByProjectId(projectId));
    }

    /**
     * Returns detailed data about the person with the given id.
     *
     * @param personId id of the person whose data to load.
     * @return the detailed data of the specified person.
     */
    public PersonDetailData loadPersonDetailData(long personId) {
        return personDetailDataMapper.map(personRepository.findDetailDataByPersonId(personId));
    }

    /**
     * Loads the data of a person together with the daily rates of that person from the database.
     *
     * @param personId ID of the person whose data to load.
     * @return PersonWithRates object
     */
    public PersonWithRates loadPersonWithRates(long personId) {
        PersonEntity personEntity = personRepository.findOneFetchDailyRates(personId);

        PersonWithRates person = new PersonWithRates();
        person.setName(personEntity.getName());
        person.setPersonId(personEntity.getId());
        person.setImportKey(personEntity.getImportKey());

        for (DailyRateEntity rateEntity : personEntity.getDailyRates()) {
            BudgetBaseData budget = new BudgetBaseData();
            budget.setId(rateEntity.getBudget().getId());
            budget.setName(rateEntity.getBudget().getName());

            PersonRate rate = new PersonRate();
            rate.setBudget(budget);
            rate.setDateRange(new DateRange(rateEntity.getDateStart(), rateEntity.getDateEnd()));
            rate.setRate(rateEntity.getRate());

            person.getRates().add(rate);
        }

        return person;
    }

    /**
     * Saves the given data about a person and his/her daily rates in the database.
     *
     * @param person the data to save in the database.
     */
    public void savePersonWithRates(PersonWithRates person) {
        PersonEntity personEntity = personRepository.findOne(person.getPersonId());
        personEntity.setName(person.getName());
        personEntity.setImportKey(person.getImportKey());

        List<DailyRateEntity> dailyRates = new ArrayList<DailyRateEntity>();
        for (PersonRate rate : person.getRates()) {
            DailyRateEntity rateEntity = new DailyRateEntity();
            rateEntity.setRate(rate.getRate());
            rateEntity.setBudget(budgetRepository.findOne(rate.getBudget().getId()));
            rateEntity.setPerson(personEntity);
            rateEntity.setDateStart(rate.getDateRange().getStartDate());
            rateEntity.setDateEnd(rate.getDateRange().getEndDate());
            dailyRates.add(rateEntity);
            recordRepository.updateDailyRates(rate.getBudget().getId(), person.getPersonId(), rate.getDateRange().getStartDate(), rate.getDateRange().getEndDate(), rate.getRate());
        }

        personEntity.setDailyRates(dailyRates);
        personRepository.save(personEntity);
    }

    /**
     * Loads the base data of a single person from the database.
     *
     * @param personId ID of the person to load.
     * @return base data of the specified person.
     */
    public PersonBaseData loadPersonBaseData(long personId) {
        return personBaseDataMapper.map(personRepository.findBaseDataByPersonId(personId));
    }
}
