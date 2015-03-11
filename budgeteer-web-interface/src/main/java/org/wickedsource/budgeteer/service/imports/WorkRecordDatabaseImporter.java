package org.wickedsource.budgeteer.service.imports;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("prototype")
public class WorkRecordDatabaseImporter extends RecordDatabaseImporter {

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private DailyRateRepository rateRepository;

    private Date earliestRecordDate = new Date(Long.MAX_VALUE);

    private Date latestRecordDate = new Date(1l);

    private List<DailyRateEntity> dailyRates;

    public WorkRecordDatabaseImporter(long projectId, String importType) {
        super(projectId, importType);
    }

    private List<List<String>> skippedRecords = new LinkedList<List<String>>();
    private SimpleDateFormat format = new SimpleDateFormat();

    @PostConstruct
    public void init() {
        super.init();
        dailyRates = getDailyRateCache(getProjectId());
    }

    @Override
    public List<List<String>> getSkippedRecords() {
        return skippedRecords;
    }

    public void importRecords(List<ImportedWorkRecord> records) {
        List<WorkRecordEntity> entitiesToImport = new ArrayList<WorkRecordEntity>();
        for (ImportedWorkRecord record : records) {

            // trimming import keys
            record.setPersonName(record.getPersonName().trim());
            record.setBudgetName(record.getBudgetName().trim());
            BudgetEntity budget = getBudget(record.getBudgetName());
            ProjectEntity project = budget.getProject();

            WorkRecordEntity entity = new WorkRecordEntity();

            entity.setPerson(getPerson(record.getPersonName()));
            entity.setBudget(budget);
            entity.setMinutes(record.getMinutesWorked());
            entity.setDate(record.getDate());
            entity.setDailyRate(getDailyRateForRecord(record));
            entity.setImportRecord(getImportRecord());

            if((project.getProjectEnd() != null && record.getDate().after(project.getProjectEnd())) ||
                    (project.getProjectStart() != null && record.getDate().before(project.getProjectStart()))){
                skippedRecords.add(getRecordAsString(entity));
            }else {
                if (record.getDate().before(earliestRecordDate)) {
                    earliestRecordDate = record.getDate();
                }
                if (record.getDate().after(latestRecordDate)) {
                    latestRecordDate = record.getDate();
                }
                entitiesToImport.add(entity);
            }
        }
        if(entitiesToImport != null  && !entitiesToImport.isEmpty()) {
            workRecordRepository.save(entitiesToImport);
        }
        //If all records haven been skipped the startDate of the import is new Date(Long.MAX_VALUE) and the EndDate is null.
        // This causes problems in our application, so they have to be set to properly values...
        if(earliestRecordDate == null || earliestRecordDate.equals(new Date(Long.MAX_VALUE))){
            earliestRecordDate = new Date();
        }
        if(latestRecordDate == null){
            latestRecordDate = new Date();
        }
        getImportRecord().setStartDate(earliestRecordDate);
        getImportRecord().setEndDate(latestRecordDate);
        getImportRecord().setNumberOfImportedFiles(getImportRecord().getNumberOfImportedFiles() + 1);
    }

    private List<String> getRecordAsString(WorkRecordEntity entity) {
        LinkedList<String> result = new LinkedList<String>();
        result.add(entity.getPerson().getName());
        result.add(entity.getBudget().getName());
        result.add("" + entity.getMinutes());
        result.add(format.format(entity.getDate()));
        result.add(entity.getDailyRate().toString());
        result.add("");
        result.add("Record is out of project-date-range");
        return  result;
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


    private List<DailyRateEntity> getDailyRateCache(long projectId) {
        return rateRepository.findByProjectIdFetch(projectId);
    }

}
