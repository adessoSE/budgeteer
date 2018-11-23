package org.wickedsource.budgeteer.service.person;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRateForBudgetBean;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.record.WorkRecord;

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
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private RecordService recordService;

    /**
     * Returns all people the given user can make use of to manage budgets.
     *
     * @param projectId ID of the project.
     * @return list of PersonBaseData objects for all people the given user can make use of to manage budgets.
     */
    @PreAuthorize("canReadProject(#projectId)")
    public List<PersonBaseData> loadPeopleBaseData(long projectId) {
        return personBaseDataMapper.map(personRepository.findBaseDataByProjectId(projectId));
    }

    /**
     * Returns detailed data about the person with the given id.
     *
     * @param personId id of the person whose data to load.
     * @return the detailed data of the specified person.
     */
    @PreAuthorize("canReadPerson(#personId)")
    public PersonDetailData loadPersonDetailData(long personId) {
        return personDetailDataMapper.map(personRepository.findDetailDataByPersonId(personId));
    }

    /**
     * Loads the data of a person together with the daily rates of that person from the database.
     *
     * @param personId ID of the person whose data to load.
     * @return PersonWithRates object
     */
    @PreAuthorize("canReadPerson(#personId)")
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

        List<DailyRateEntity> dailyRates = new ArrayList<>();

        for (PersonRate rate : loadPersonWithRates(person.getPersonId()).getRates()) {
            workRecordRepository.updateDailyRates(rate.getBudget().getId(), person.getPersonId(),
                    rate.getDateRange().getStartDate(), rate.getDateRange().getEndDate(), Money.zero(CurrencyUnit.EUR));
        }

        for (PersonRate rate : person.getRates()) {
            DailyRateEntity rateEntity = new DailyRateEntity();
            rateEntity.setRate(rate.getRate());
            rateEntity.setBudget(budgetRepository.findOne(rate.getBudget().getId()));
            rateEntity.setPerson(personEntity);
            rateEntity.setDateStart(rate.getDateRange().getStartDate());
            rateEntity.setDateEnd(rate.getDateRange().getEndDate());
            dailyRates.add(rateEntity);

            workRecordRepository.updateDailyRates(rate.getBudget().getId(), person.getPersonId(),
                    rate.getDateRange().getStartDate(), rate.getDateRange().getEndDate(), rate.getRate());
        }
        personEntity.getDailyRates().clear();
        personEntity.getDailyRates().addAll(dailyRates);
        personRepository.save(personEntity);
    }

    public List<String> getOverlapWithManuallyEditedRecords(PersonWithRates person, long projectId){
        List<String> warnings = new ArrayList<>();
        //Check with manually edited entries and warn the user
        for(PersonRate rate : person.getRates()) {
            for (WorkRecordEntity e : workRecordRepository.findManuallyEditedEntries(projectId,
                    rate.getDateRange().getStartDate(), rate.getDateRange().getEndDate())) {

                //Warn about the editing of a rate only if a work record in this range has been edited manually and the amount is different
                if (DateUtil.isDateInDateRange(e.getDate(), rate.getDateRange())
                        && e.getBudget().getName().equals(rate.getBudget().getName())
                        && e.getPerson().getName().equals(person.getName())
                        && !e.getDailyRate().isEqual(() -> rate.getRate().toBigMoney())) {

                    warnings.add("A work record in the range "
                            + rate.getDateRange().toString()
                            + " (Exact Date and Amount: " + e.getDate() + ", " + e.getDailyRate().toString() +
                            ") for budget \"" + rate.getBudget().getName() +
                            "\" has already been edited manually and will not be overwritten.");
                }
            }
        }
        return warnings;
    }

    /**
     * Loads the base data of a single person from the database.
     *
     * @param personId ID of the person to load.
     * @return base data of the specified person.
     */
    @PreAuthorize("canReadPerson(#personId)")
    public PersonBaseData loadPersonBaseData(long personId) {
        return personBaseDataMapper.map(personRepository.findBaseDataByPersonId(personId));
    }

    @PreAuthorize("canReadPerson(#personId)")
    public void deletePerson(long personId) {
        personRepository.delete(personId);
    }

    @PreAuthorize("canReadBudget(#budgetId)")
    public List<PersonBaseData> loadPeopleBaseDataByBudget(long budgetId) {
        return personBaseDataMapper.map(personRepository.findBaseDataByBudgetId(budgetId));
    }

    public List<MissingDailyRateForBudgetBean> getMissingDailyRatesForPerson(long personId) {
        return recordService.getMissingDailyRatesForPerson(personId);
    }

    public void removeDailyRateFromPerson(PersonWithRates personWithRates, PersonRate rate) {
        List<WorkRecordEntity> records = workRecordRepository.findByPersonId(personWithRates.getPersonId());
        for(WorkRecordEntity record : records){
            if(record.getBudget().getName().equals(rate.getBudget().getName()) && DateUtil.isDateInDateRange(record.getDate(), rate.getDateRange())){
                record.setDailyRate(Money.zero(CurrencyUnit.EUR));
                workRecordRepository.save(record);
            }
        }
    }
}
