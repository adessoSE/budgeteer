package org.wickedsource.budgeteer.service.imports;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.imports.api.ImportedPlanRecord;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Scope("prototype")
public class PlanRecordDatabaseImporter extends RecordDatabaseImporter {

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private DailyRateRepository dailyRateRepository;

    private List<List<String>> skippedRecords;

    private SimpleDateFormat formatter = new SimpleDateFormat();

    public PlanRecordDatabaseImporter(long projectId, String importType) {
        super(projectId, importType);
    }

    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public List<List<String>> getSkippedRecords() {
        return skippedRecords;
    }

    public void importRecords(List<ImportedPlanRecord> records) {
        skippedRecords = new LinkedList<List<String>>();
        Map<RecordKey, List<ImportedPlanRecord>> groupedRecords = groupRecords(records);
        for (RecordKey key : groupedRecords.keySet()) {
            List<ImportedPlanRecord> importedPlanRecords = groupedRecords.get(key);
            List<List<String>> skippedPlanRecords = importRecordGroup(key, importedPlanRecords);
            if(skippedPlanRecords != null && !skippedPlanRecords.isEmpty()){
                skippedRecords.addAll(skippedPlanRecords);
            }
        }
        //If all records haven been skipped the startDate of the import is new Date(Long.MAX_VALUE) and the EndDate is null.
        // This causes problems in our application, so they have to be set to properly values...
        if(getImportRecord().getStartDate() == null || getImportRecord().getStartDate().equals(new Date(Long.MAX_VALUE))){
            getImportRecord().setStartDate(new Date());
        }
        if(getImportRecord().getEndDate() == null){
            getImportRecord().setEndDate(new Date());
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
    private List<List<String>> importRecordGroup(RecordKey groupKey, List<ImportedPlanRecord> records) {
        List<List<String>> skippedRecords = new LinkedList<List<String>>();
        Date earliestDate = new Date(Long.MAX_VALUE);
        Date latestDate = new Date(0);

        BudgetEntity budget = getBudget(groupKey.getBudgetName());
        PersonEntity person = getPerson(groupKey.getPersonName());
        ProjectEntity project = budget.getProject();

        List<PlanRecordEntity> entitiesToImport = new ArrayList<PlanRecordEntity>();
        for (ImportedPlanRecord record : records) {
            PlanRecordEntity recordEntity = new PlanRecordEntity();
            recordEntity.setDate(record.getDate());
            recordEntity.setPerson(person);
            recordEntity.setBudget(budget);
            recordEntity.setMinutes(record.getMinutesPlanned());
            recordEntity.setImportRecord(getImportRecord());
            recordEntity.setDailyRate(record.getDailyRate());

            //Check whether there are existing Planrecords in the Database for this Date, Budget and Person
            List<PlanRecordEntity> existingRecords = planRecordRepository.findByPersonBudgetDate(person.getId(), budget.getId(), record.getDate());
            if(existingRecords.size() > 1){
                skippedRecords.add(getRecordAsString(recordEntity, "More than one Record for this Budget, Person and Date found"));
            }
            else if((project.getProjectEnd() != null && record.getDate().after(project.getProjectEnd())) ||
                    (project.getProjectStart() != null && record.getDate().before(project.getProjectStart()))){
                skippedRecords.add(getRecordAsString(recordEntity));
            }else {
                if(existingRecords.size() == 1){
                    //update the existing record
                    recordEntity = existingRecords.get(0);
                    recordEntity.setMinutes(record.getMinutesPlanned());
                    recordEntity.setImportRecord(getImportRecord());
                    recordEntity.setDailyRate(record.getDailyRate());
                }
                entitiesToImport.add(recordEntity);

                if (record.getDate().after(latestDate)) {
                    latestDate = record.getDate();
                }
                if (record.getDate().before(earliestDate)) {
                    earliestDate = record.getDate();
                }
            }
        }
        if(entitiesToImport != null && !entitiesToImport.isEmpty()) {
            planRecordRepository.save(entitiesToImport);

            // updating start and end date for import record
            if (getImportRecord().getStartDate() == null || getImportRecord().getStartDate().after(earliestDate)) {
                getImportRecord().setStartDate(earliestDate);
            }
            if (getImportRecord().getEndDate() == null || getImportRecord().getEndDate().before(latestDate)) {
                getImportRecord().setEndDate(latestDate);
            }
        }
        return skippedRecords;
    }

    private List<String> getRecordAsString(PlanRecordEntity recordEntity) {
        return getRecordAsString(recordEntity, "Record is out of project-date-range");
    }

    private List<String> getRecordAsString(PlanRecordEntity recordEntity, String reason) {
        List<String> result = new LinkedList<String>();
        result.add(recordEntity.getDate() != null ? formatter.format(recordEntity.getDate()) : "");
        result.add(recordEntity.getPerson().getName());
        result.add(recordEntity.getBudget().getName());
        result.add(""+recordEntity.getMinutes());
        result.add(recordEntity.getDailyRate() != null ? recordEntity.getDailyRate().toString() : "");
        result.add(reason);
        return result;
    }

    @Data
    @AllArgsConstructor
    class RecordKey {
        private String personName;
        private String budgetName;
        private Money dailyRate;

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
