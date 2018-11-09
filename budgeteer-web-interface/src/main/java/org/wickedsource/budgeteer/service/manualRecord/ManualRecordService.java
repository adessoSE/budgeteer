package org.wickedsource.budgeteer.service.manualRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualWorkRecordEntity;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualWorkRecordRepository;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManualRecordService {
    @Autowired
    private ManualWorkRecordRepository manualWorkRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    public List<ManualRecord> getManualRecords(long budgetId) {

        List<ManualWorkRecordEntity> entities = manualWorkRecordRepository.getManualRecordByBudgetId(budgetId);
        List<ManualRecord> result = new ArrayList<>();

        for (ManualWorkRecordEntity entity : entities) {
            ManualRecord data = new ManualRecord(entity);
            result.add(data);
        }

        return result;
    }

    public void saveManualRecord(ManualRecord data) {
        assert data != null;

        ManualWorkRecordEntity record = new ManualWorkRecordEntity();
        record.setDescription(data.getDescription());
        record.setCents((int) (MoneyUtil.toDouble(data.getMoneyAmount()) * 100));
        record.setDate(new Date());
        BudgetEntity budgetEntity = budgetRepository.findOne(data.getBudgetId());
        record.setBudget(budgetEntity);

        manualWorkRecordRepository.save(record);
    }

    public  void addManualRecordsByWeekAndPersonForBudgetWithTax(List<WeeklyAggregatedRecordWithTitleAndTaxBean> workRecords, long budgetId)
    {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> manualWorkRecords = manualWorkRecordRepository.aggregateByWeekForBudgetWithTax(budgetId);
        workRecords.addAll(manualWorkRecords);
    }

    public void addManualRecordsByMonthAndBudgetWithTax( List<MonthlyAggregatedRecordWithTaxBean> workRecords, long budgetId)
    {
        List<MonthlyAggregatedRecordWithTaxBean> manualWorkRecords = manualWorkRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        workRecords.addAll(manualWorkRecords);
    }

    public void addManualRecordsByWeekForBudgetsWithTax( List<WeeklyAggregatedRecordWithTitleAndTaxBean> workRecords, long projectId)
    {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean>  manualWorkRecords = manualWorkRecordRepository.aggregateByWeekForBudgetsWithTax(projectId);
        workRecords.addAll(manualWorkRecords);
    }

    public void addManualRecordsByWeekForBudgetsWithTax(List<WeeklyAggregatedRecordWithTitleAndTaxBean> workRecords, long projectId, List<String> filter)
    {
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> manualWorkRecords = manualWorkRecordRepository.aggregateByWeekForBudgetsWithTax(projectId, filter);
        workRecords.addAll(manualWorkRecords);
    }

    public void addManualRecordsByMonthAndBudgetTagsWithTax(List<MonthlyAggregatedRecordWithTaxBean> workRecords, long projectId)
    {
        List<MonthlyAggregatedRecordWithTaxBean> manualWorkRecords = manualWorkRecordRepository.aggregateByMonthWithTax(projectId);
        workRecords.addAll(manualWorkRecords);
    }

    public void addManualRecordsByMonthAndBudgetTagsWithTax(List<MonthlyAggregatedRecordWithTaxBean> workRecords, long projectId, List<String> filter)
    {
        List<MonthlyAggregatedRecordWithTaxBean> manualWorkRecords = manualWorkRecordRepository.aggregateByMonthAndBudgetTagsWithTax(projectId, filter);
        workRecords.addAll(manualWorkRecords);
    }
}
