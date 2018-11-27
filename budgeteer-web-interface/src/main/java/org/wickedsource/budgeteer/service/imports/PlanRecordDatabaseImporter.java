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
import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    public void importRecords(List<ImportedPlanRecord> records, String filename) {
        skippedRecords = new LinkedList<>();
        Map<RecordKey, List<ImportedPlanRecord>> groupedRecords = groupRecords(records);

        //Because of Issue #70 all PlanRecords that are after the earliest of the imported ones, should be deleted

        // Delete only records of the imported budgets
        List<String> budgetNames = getBudgetNames(records);
        for (String name : budgetNames) {
            // Select which record to delete for each budget individually
            List<ImportedPlanRecord> budgetRecords = getRecordsOfBudget(name, records);
            Date earliestDate = findEarliestDate(budgetRecords);
            planRecordRepository.deleteByBudgetKeyAndDate(name, earliestDate);
        }

        for (RecordKey key : groupedRecords.keySet()) {
            List<ImportedPlanRecord> importedPlanRecords = groupedRecords.get(key);
            List<List<String>> skippedPlanRecords = importRecordGroup(key, importedPlanRecords, filename);
            if (skippedPlanRecords != null && !skippedPlanRecords.isEmpty()) {
                skippedRecords.addAll(skippedPlanRecords);
            }
        }
        //If all records haven been skipped the startDate of the import is new Date(Long.MAX_VALUE) and the EndDate is null.
        // This causes problems in our application, so they have to be set to properly values...
        if (getImportRecord().getStartDate() == null || getImportRecord().getStartDate().equals(new Date(Long.MAX_VALUE))) {
            getImportRecord().setStartDate(new Date());
        }
        if (getImportRecord().getEndDate() == null) {
            getImportRecord().setEndDate(new Date());
        }
        getImportRecord().setNumberOfImportedFiles(getImportRecord().getNumberOfImportedFiles() + 1);
    }

    private Date findEarliestDate(List<ImportedPlanRecord> records) {
        if (records == null || records.isEmpty()) return null;
        Date da = new Date(Long.MAX_VALUE);
        for (ImportedPlanRecord pla : records) {
            if (pla.getDate().before(da)) {
                da = pla.getDate();
            }
        }
        return da;
    }

    /**
     * Get the names of all budgets in the imported records
     *
     * @param records imported records
     * @return budget names of the imported records
     */
    private List<String> getBudgetNames(List<ImportedPlanRecord> records) {
        List<String> names = new ArrayList<>();

        for (ImportedPlanRecord record : records) {
            String name = record.getBudgetName();
            if (!names.contains(name)) {
                names.add(name);
            }
        }

        return names;
    }

    /**
     * Get all records of one budget
     *
     * @param budgetname name of the budget
     * @param records    import records
     * @return records of the budget
     */
    private List<ImportedPlanRecord> getRecordsOfBudget(String budgetname, List<ImportedPlanRecord> records) {
        return records.stream().filter(r -> r.getBudgetName().equals(budgetname)).collect(Collectors.toList());
    }

    private Map<RecordKey, List<ImportedPlanRecord>> groupRecords(List<ImportedPlanRecord> records) {
        Map<RecordKey, List<ImportedPlanRecord>> result = new HashMap<>();
        for (ImportedPlanRecord record : records) {
            RecordKey key = new RecordKey(record.getPersonName(), record.getBudgetName(), record.getDailyRate());
            List<ImportedPlanRecord> keyList = result.get(key);
            if (keyList == null) {
                keyList = new ArrayList<>();
                result.put(key, keyList);
            }
            keyList.add(record);
        }
        return result;
    }

    /**
     * Imports a list of records with the same person, budget and daily rate.
     */
    private List<List<String>> importRecordGroup(RecordKey groupKey, List<ImportedPlanRecord> records, String filename) {
        List<List<String>> skippedRecords = new LinkedList<>();
        Date earliestDate = new Date(Long.MAX_VALUE);
        Date latestDate = new Date(0);

        BudgetEntity budget = getBudget(groupKey.getBudgetName());
        PersonEntity person = getPerson(groupKey.getPersonName());
        ProjectEntity project = budget.getProject();

        List<PlanRecordEntity> entitiesToImport = new ArrayList<>();
        for (ImportedPlanRecord record : records) {
            PlanRecordEntity recordEntity = new PlanRecordEntity();
            recordEntity.setDate(record.getDate());
            recordEntity.setPerson(person);
            recordEntity.setBudget(budget);
            recordEntity.setMinutes(record.getMinutesPlanned());
            recordEntity.setImportRecord(getImportRecord());
            recordEntity.setDailyRate(record.getDailyRate());

            if ((project.getProjectEnd() != null && record.getDate().after(project.getProjectEnd())) ||
                    (project.getProjectStart() != null && record.getDate().before(project.getProjectStart()))) {
                skippedRecords.add(getRecordAsString(recordEntity, filename));
            } else {

                entitiesToImport.add(recordEntity);

                if (record.getDate().after(latestDate)) {
                    latestDate = record.getDate();
                }
                if (record.getDate().before(earliestDate)) {
                    earliestDate = record.getDate();
                }
            }
        }
        if (entitiesToImport != null && !entitiesToImport.isEmpty()) {
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

    private List<String> getRecordAsString(PlanRecordEntity recordEntity, String filename) {
        return getRecordAsString(recordEntity, filename, "Record is out of project-date-range");
    }

    private List<String> getRecordAsString(PlanRecordEntity recordEntity, String filename, String reason) {
        List<String> result = new LinkedList<>();
        result.add(filename);
        result.add(recordEntity.getDate() != null ? formatter.format(recordEntity.getDate()) : "");
        result.add(recordEntity.getPerson().getName());
        result.add(recordEntity.getBudget().getName());
        result.add("" + recordEntity.getMinutes());
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
