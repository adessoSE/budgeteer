package org.wickedsource.budgeteer.service.imports;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
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

    @PostConstruct
    public void init() {
        super.init();
        dailyRates = getDailyRateCache(getProjectId());
    }

    public void importRecords(List<ImportedWorkRecord> records) {
        List<WorkRecordEntity> entitiesToImport = new ArrayList<WorkRecordEntity>();
        for (ImportedWorkRecord record : records) {

            // trimming import keys
            record.setPersonName(record.getPersonName().trim());
            record.setBudgetName(record.getBudgetName().trim());

            WorkRecordEntity entity = new WorkRecordEntity();

            entity.setPerson(getPerson(record.getPersonName()));
            entity.setBudget(getBudget(record.getBudgetName()));
            entity.setMinutes(record.getMinutesWorked());
            entity.setDate(record.getDate());
            entity.setDailyRate(getDailyRateForRecord(record));
            entity.setImportRecord(getImportRecord());

            if (record.getDate().before(earliestRecordDate)) {
                earliestRecordDate = record.getDate();
            }
            if (record.getDate().after(latestRecordDate)) {
                latestRecordDate = record.getDate();
            }
            entitiesToImport.add(entity);
        }
        workRecordRepository.save(entitiesToImport);
        getImportRecord().setStartDate(earliestRecordDate);
        getImportRecord().setEndDate(latestRecordDate);
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
