package de.adesso.budgeteer.persistence.record;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.service.record.PlanAndWorkRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListJoiner {
    /**
     * Sums the hours of weekly plan-beans with the same year, month, week and tax rate.
     *
     * @param beans List of the weekly plan-beans that should be joined
     * @return new list with joined plan-beans
     */
    public static List<WeeklyAggregatedRecordWithTaxBean> joinPlanBeanHours(List<WeeklyAggregatedRecordWithTaxBean> beans) {
        HashMap<String, WeeklyAggregatedRecordWithTaxBean> planMap = new HashMap<>();
        for (WeeklyAggregatedRecordWithTaxBean bean : beans) {
            WeeklyAggregatedRecordWithTaxBean record = planMap.get(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()));
            if (record == null) {
                planMap.put(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()), bean);
            } else {
                record.setHours(record.getHours() + bean.getHours());
            }
        }
        return new ArrayList<>(planMap.values());
    }

    /**
     * Sums the hours of weekly work-beans with the same year, month, week and tax rate.
     *
     * @param beans List of the weekly work-beans that should be joined
     * @return new list with joined work-beans
     */
    public static List<WeeklyAggregatedRecordWithTitleAndTaxBean> joinWorkBeanHours(List<WeeklyAggregatedRecordWithTitleAndTaxBean> beans) {
        HashMap<String, WeeklyAggregatedRecordWithTitleAndTaxBean> workMap = new HashMap<>();
        for (WeeklyAggregatedRecordWithTitleAndTaxBean bean : beans) {
            WeeklyAggregatedRecordWithTitleAndTaxBean record = workMap.get(String.format("%d%d%d%s%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate(), bean.getTitle()));
            if (record == null) {
                workMap.put(String.format("%d%d%d%s%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate(), bean.getTitle()), bean);
            } else {
                record.setHours(record.getHours() + bean.getHours());
            }
        }
        return new ArrayList<>(workMap.values());
    }

    /**
     * Joins a list with weekly plan beans and a list with weekly work beans to a list of PlanAndWorkRecords
     *
     * @param planList list with weekly plan beans
     * @param workList list with weekly weekly work beans
     * @return joined beans as a list of PlanAndWorkRecords
     */
    public static List<PlanAndWorkRecord> joinWeeklyToPlanAndWorkRecords(List<WeeklyAggregatedRecordWithTaxBean> planList, List<WeeklyAggregatedRecordWithTitleAndTaxBean> workList) {
        HashMap<String, PlanAndWorkRecord> planAndWorkMap = new HashMap<>();
        for (WeeklyAggregatedRecordWithTaxBean bean : planList) {
            PlanAndWorkRecord record = planAndWorkMap.get(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()));
            if (record == null) {
                planAndWorkMap.put(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()), new PlanAndWorkRecord(bean));
            } else {
                record.setValueInCentsPlanned(record.getValueInCentsPlanned() + bean.getValueInCents());
                record.setValueInCentsPlanned_gross(MoneyUtil.getCentsWithTaxes(record.getValueInCentsPlanned(), record.getTaxRate()));
                record.setHoursPlanned(record.getHoursPlanned() + bean.getHours());
            }
        }
        for (WeeklyAggregatedRecordWithTitleAndTaxBean bean : workList) {
            PlanAndWorkRecord record = planAndWorkMap.get(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()));
            if (record == null) {
                planAndWorkMap.put(String.format("%d%d%d%s", bean.getYear(), bean.getMonth(), bean.getWeek(), bean.getTaxRate()), new PlanAndWorkRecord(bean));
            } else {
                record.setValueInCentsBurned(record.getValueInCentsBurned() + bean.getValueInCents());
                record.setValueInCentsBurned_gross(MoneyUtil.getCentsWithTaxes(record.getValueInCentsBurned(), record.getTaxRate()));
                record.setHoursWorked(record.getHoursWorked() + bean.getHours());
            }
        }
        return new ArrayList<>(planAndWorkMap.values());
    }

    /**
     * Joins a list with monthly plan beans and a list with monthly work beans to a list of PlanAndWorkRecords
     *
     * @param planList list with monthly plan beans
     * @param workList list with monthly work beans
     * @return joined beans as a list of PlanAndWorkRecords
     */
    public static List<PlanAndWorkRecord> joinMonthlyToPlanAndWorkRecords(List<MonthlyAggregatedRecordWithTaxBean> planList, List<MonthlyAggregatedRecordWithTaxBean> workList) {
        HashMap<String, PlanAndWorkRecord> planAndWorkMap = new HashMap<>();

        for (MonthlyAggregatedRecordWithTaxBean planBean : planList) {
            PlanAndWorkRecord record = planAndWorkMap.get(String.format("%s%d%d", planBean.getTaxRate(), planBean.getYear(), planBean.getMonth()));
            if (record == null) {
                planAndWorkMap.put(String.format("%s%d%d", planBean.getTaxRate(), planBean.getYear(), planBean.getMonth()), new PlanAndWorkRecord(planBean, true));
            } else {
                record.setValueInCentsPlanned(record.getValueInCentsPlanned() + planBean.getValueInCents());
                record.setValueInCentsPlanned_gross(MoneyUtil.getCentsWithTaxes(record.getValueInCentsPlanned(), record.getTaxRate()));
                record.setHoursPlanned(record.getHoursPlanned() + planBean.getHours());
            }
        }
        for (MonthlyAggregatedRecordWithTaxBean workBean : workList) {
            PlanAndWorkRecord record = planAndWorkMap.get(String.format("%s%d%d", workBean.getTaxRate(), workBean.getYear(), workBean.getMonth()));
            if (record == null) {
                planAndWorkMap.put(String.format("%s%d%d", workBean.getTaxRate(), workBean.getYear(), workBean.getMonth()), new PlanAndWorkRecord(workBean, false));
            } else {
                record.setValueInCentsBurned(record.getValueInCentsBurned() + workBean.getValueInCents());
                record.setValueInCentsBurned_gross(MoneyUtil.getCentsWithTaxes(record.getValueInCentsBurned(), record.getTaxRate()));
                record.setHoursWorked(record.getHoursWorked() + workBean.getHours());
            }
        }
        return new ArrayList<>(planAndWorkMap.values());
    }
}
