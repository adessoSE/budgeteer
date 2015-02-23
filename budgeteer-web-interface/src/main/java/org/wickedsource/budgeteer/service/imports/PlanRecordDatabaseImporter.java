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

    public void importRecords(List<ImportedPlanRecord> records) {
        Map<RecordKey, List<ImportedPlanRecord>> groupedRecords = groupRecords(records);
        for (RecordKey key : groupedRecords.keySet()) {
            importRecordGroup(key, groupedRecords.get(key));
        }
    }

    private Map<RecordKey, List<ImportedPlanRecord>> groupRecords(List<ImportedPlanRecord> records) {
        Map<RecordKey, List<ImportedPlanRecord>> result = new HashMap<RecordKey, List<ImportedPlanRecord>>();
        for (ImportedPlanRecord record : records) {
            RecordKey key = new RecordKey(record.getPersonName(), record.getBudgetName(), record.getDailyRate());
            List<ImportedPlanRecord> keyList = result.get(key);
            if (keyList == null) {
                keyList = new ArrayList<ImportedPlanRecord>();
                result.put(key, keyList);
            }
            keyList.add(record);
        }
        return result;
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
            List<DailyRateEntity> dailyRatesThatEndAfterTheNewRate = dailyRateRepository.findByBudgetAndPersonEndingInOrAfterDateRange(budget.getId(), person.getId(), earliestDate);
            for(int i=dailyRatesThatEndAfterTheNewRate.size() -1; i >=0; i--){
                DailyRateEntity dailyRate = dailyRatesThatEndAfterTheNewRate.get(i);
                //if the dailyRate in the database is completely in the future, delete it
                if(dailyRate.getDateStart().compareTo(earliestDate) >= 0){
                    dailyRateRepository.delete(dailyRate);
                    dailyRatesThatEndAfterTheNewRate.remove(dailyRate);
                    continue;
                }
                // if the dailyRate in our database starts in the past and continues farther to the future,
                // alter the endDate to the beginning of the new imported record minus 1 day
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(earliestDate);
                cal.add(Calendar.DAY_OF_YEAR, -1);
                dailyRate.setDateEnd(cal.getTime());
                dailyRateRepository.save(dailyRate);
            }
            // if there weren't any dailyRates in the database that were altered or they were truncated, insert a new dailyRate for the future
            DailyRateEntity dailyRate = new DailyRateEntity();
            dailyRate.setBudget(budget);
            dailyRate.setPerson(person);
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
