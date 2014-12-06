package org.wickedsource.budgeteer.service.imports;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.ImportedPlanRecord;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Scope("prototype")
public class PlanRecordDatabaseImporter extends RecordDatabaseImporter {

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private DailyRateRepository dailyRateRepository;

    public PlanRecordDatabaseImporter(long projectId, String importType) {
        super(projectId, importType);
    }

    @PostConstruct
    public void init() {
        super.init();
    }

    private Map<RecordKey, List<ImportedPlanRecord>> groupedRecords = new HashMap<RecordKey, List<ImportedPlanRecord>>();

    public void importRecords(List<ImportedPlanRecord> records) {
        groupRecords(records);
        for (RecordKey key : groupedRecords.keySet()) {
            importRecordGroup(key, groupedRecords.get(key));
        }
    }

    private void groupRecords(List<ImportedPlanRecord> records) {
        for (ImportedPlanRecord record : records) {
            RecordKey key = new RecordKey(record.getPersonName(), record.getBudgetName(), record.getDailyRate());
            List<ImportedPlanRecord> keyList = groupedRecords.get(key);
            if (keyList == null) {
                keyList = new ArrayList<ImportedPlanRecord>();
                groupedRecords.put(key, keyList);
            }
            keyList.add(record);
        }
    }

    /**
     * Imports a list of records with the same person, budget and daily rate.
     */
    private void importRecordGroup(RecordKey groupKey, List<ImportedPlanRecord> records) {
        Date earliestDate = new Date(Long.MAX_VALUE);
        Date latestDate = new Date(0);

        BudgetEntity budget = getBudget(groupKey.getBudgetName());
        PersonEntity person = getPerson(groupKey.getPersonName());

        List<PlanRecordEntity> entitiesToImport = new ArrayList<PlanRecordEntity>();
        for (ImportedPlanRecord record : records) {
            PlanRecordEntity recordEntity = new PlanRecordEntity();
            recordEntity.setDate(record.getDate());
            recordEntity.setPerson(person);
            recordEntity.setBudget(budget);
            recordEntity.setMinutes(record.getMinutesPlanned());
            recordEntity.setImportRecord(getImportRecord());
            recordEntity.setDailyRate(record.getDailyRate());
            entitiesToImport.add(recordEntity);

            if (record.getDate().after(latestDate)) {
                latestDate = record.getDate();
            }
            if (record.getDate().before(earliestDate)) {
                earliestDate = record.getDate();
            }
        }

        planRecordRepository.save(entitiesToImport);

        if (!entitiesToImport.isEmpty()) {
            // creating daily rate or updating it with new start and end dates
            DailyRateEntity dailyRate = dailyRateRepository.findByBudgetAndPersonInDateRange(budget.getId(), person.getId(), earliestDate, latestDate);
            if (dailyRate == null) {
                dailyRate = new DailyRateEntity();
                dailyRate.setBudget(budget);
                dailyRate.setPerson(person);
            }
            dailyRate.setRate(groupKey.getDailyRate());
            dailyRate.setDateStart(earliestDate);
            dailyRate.setDateEnd(latestDate);
            dailyRateRepository.save(dailyRate);
        }

        // updating start and end date for import record
        if (getImportRecord().getStartDate() == null || getImportRecord().getStartDate().after(earliestDate)) {
            getImportRecord().setStartDate(earliestDate);
        }
        if (getImportRecord().getEndDate() == null || getImportRecord().getEndDate().before(latestDate)) {
            getImportRecord().setEndDate(latestDate);
        }
    }

    class RecordKey {
        private String personName;

        private String budgetName;

        private Money dailyRate;

        public RecordKey(String personName, String budgetName, Money dailyRate) {
            this.personName = personName;
            this.budgetName = budgetName;
            this.dailyRate = dailyRate;
        }

        public String getPersonName() {
            return personName;
        }

        public String getBudgetName() {
            return budgetName;
        }

        public Money getDailyRate() {
            return dailyRate;
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

}
