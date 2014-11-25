package org.wickedsource.budgeteer.service.imports;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Scope("prototype")
public class WorkRecordDatabaseImporter {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private DailyRateRepository rateRepository;

    private Map<String, PersonEntity> personsByImportKey;

    private Map<String, BudgetEntity> budgetsByImportKey;

    private ProjectEntity project;

    private ImportEntity importRecord;

    private Date earliestRecordDate = new Date(Long.MAX_VALUE);

    private Date latestRecordDate = new Date(1l);

    private List<DailyRateEntity> dailyRates;

    private long projectId;

    private String importType;

    public WorkRecordDatabaseImporter(long projectId, String importType) {
        this.projectId = projectId;
        this.importType = importType;
    }

    @PostConstruct
    public void init() {
        project = projectRepository.findOne(projectId);
        budgetsByImportKey = getBudgetCache(projectId);
        personsByImportKey = getPersonCache(projectId);
        dailyRates = getDailyRateCache(projectId);

        importRecord = new ImportEntity();
        importRecord.setProject(project);
        importRecord.setImportType(importType);
        importRecord.setImportDate(new Date());
        importRepository.save(importRecord);
    }

    public void importRecords(List<ImportedWorkRecord> records) {
        List<WorkRecordEntity> entitiesToImport = new ArrayList<WorkRecordEntity>();
        for (ImportedWorkRecord record : records) {
            WorkRecordEntity entity = new WorkRecordEntity();

            entity.setPerson(getPersonForRecord(record));
            entity.setBudget(getBudgetForRecord(record));
            entity.setMinutes(record.getMinutesWorked());
            entity.setDate(record.getDate());
            entity.setDailyRate(getDailyRateForRecord(record));
            entity.setImportRecord(importRecord);

            if (record.getDate().before(earliestRecordDate)) {
                earliestRecordDate = record.getDate();
            }
            if (record.getDate().after(latestRecordDate)) {
                latestRecordDate = record.getDate();
            }
            entitiesToImport.add(entity);
        }
        workRecordRepository.save(entitiesToImport);
        importRecord.setStartDate(earliestRecordDate);
        importRecord.setEndDate(latestRecordDate);
    }

    private Money getDailyRateForRecord(ImportedWorkRecord record) {
        for (DailyRateEntity rate : dailyRates) {
            // TODO: this is far from performant, should be reworked when large data proves to be an issue
            if (rate.getBudget().getImportKey().equals(record.getBudgetName()) &&
                    rate.getPerson().getImportKey().equals(record.getPersonName()) &&
                    !rate.getDateStart().after(record.getDate()) &&
                    !rate.getDateEnd().before(record.getDate())) {
                return rate.getRate();
            }
        }
        return MoneyUtil.createMoneyFromCents(0l);
    }

    /**
     * Looks if the person matching the given record's person import key is in the cache. If yes, take it from the cache.
     * If not, create a new person and put it into the cache.
     */
    private PersonEntity getPersonForRecord(ImportedWorkRecord record) {
        PersonEntity person = personsByImportKey.get(record.getPersonName());
        if (person == null) {
            PersonEntity newPerson = new PersonEntity();
            newPerson.setImportKey(record.getPersonName());
            newPerson.setProject(project);
            newPerson.setName(record.getPersonName());
            personRepository.save(newPerson);
            personsByImportKey.put(newPerson.getImportKey(), newPerson);
            person = newPerson;
        }
        return person;
    }

    /**
     * Looks if the budget matching the given record's budget import key is in the cache. If yes, take it from the cache.
     * If not, create a new budget and put it into the cache.
     */
    private BudgetEntity getBudgetForRecord(ImportedWorkRecord record) {
        BudgetEntity budget = budgetsByImportKey.get(record.getBudgetName());
        if (budget == null) {
            BudgetEntity newBudget = new BudgetEntity();
            newBudget.setImportKey(record.getBudgetName());
            newBudget.setName(record.getBudgetName());
            newBudget.setProject(project);
            newBudget.setTotal(MoneyUtil.createMoneyFromCents(0l));
            budgetRepository.save(newBudget);
            budgetsByImportKey.put(newBudget.getImportKey(), newBudget);
            budget = newBudget;
        }
        return budget;
    }


    private List<DailyRateEntity> getDailyRateCache(long projectId) {
        return rateRepository.findByProjectIdFetch(projectId);
    }

    /**
     * Creates a map of persons import keys to the person entities to be used as a cache. This cache can be used to prevent
     * repeated access to the database.
     */
    private Map<String, PersonEntity> getPersonCache(long projectId) {
        Map<String, PersonEntity> map = new HashMap<String, PersonEntity>();
        List<PersonEntity> persons = personRepository.findByProjectIdOrderByNameAsc(projectId);
        for (PersonEntity person : persons) {
            map.put(person.getImportKey(), person);
        }
        return map;
    }

    /**
     * Creates a map of budgets import keys to the budget entities to be used as a cache. This cache can be used to prevent
     * repeated access to the database.
     */
    private Map<String, BudgetEntity> getBudgetCache(long projectId) {
        Map<String, BudgetEntity> map = new HashMap<String, BudgetEntity>();
        List<BudgetEntity> budgets = budgetRepository.findByProjectIdOrderByNameAsc(projectId);
        for (BudgetEntity budget : budgets) {
            map.put(budget.getImportKey(), budget);
        }
        return map;
    }
}
