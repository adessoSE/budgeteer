package org.wickedsource.budgeteer.service.manualRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordEntity;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordRepository;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ManualRecordService {
    @Autowired
    private ManualRecordRepository manualRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    public List<ManualRecord> getManualRecords(long budgetId) {

        List<ManualRecordEntity> entities = manualRecordRepository.getManualRecordByBudgetId(budgetId);
        List<ManualRecord> result = new ArrayList<>();

        for (ManualRecordEntity entity : entities) {
            ManualRecord data = new ManualRecord(entity);
            result.add(data);
        }

        return result;
    }

    public void saveManualRecord(ManualRecord data) {
        assert data != null;

        ManualRecordEntity record = new ManualRecordEntity();
        record.setDescription(data.getDescription());
        record.setCents((int) (MoneyUtil.toDouble(data.getMoneyAmount()) * 100));
        record.setDate(new Date());
        BudgetEntity budgetEntity = budgetRepository.findOne(data.getBudgetId());
        record.setBudget(budgetEntity);

        manualRecordRepository.save(record);
    }
}
