package org.wickedsource.budgeteer.service.record;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean;

import java.util.*;

@Component
public class RecordJoiner {

    /**
     * Joins the given work records and the given plan records since it is not possible to join them in the database with JPA.
     */
    public List<AggregatedRecord> joinWeekly(List<WeeklyAggregatedRecordBean> workRecords, List<WeeklyAggregatedRecordBean> planRecords) {
        RecordMap recordMap = new RecordMap();
        for (WeeklyAggregatedRecordBean bean : planRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecord(bean);
            record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
        }
        for (WeeklyAggregatedRecordBean bean : workRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecord(bean);
            record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setHours(bean.getHours());
        }
        List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
        Collections.sort(records, new AggregatedRecordComparator());
        return records;
    }

    /**
     * Joins the given work records and the given plan records with calculating the taxes since it is not possible to join them in the database with JPA.
     */
    public List<AggregatedRecord> joinWeeklyWithTax(List<WeeklyAggregatedRecordWithTaxBean> workRecords, List<WeeklyAggregatedRecordWithTaxBean> planRecords) {
        RecordMap recordMap = new RecordMap();

        // Create a record for each bean
        for (WeeklyAggregatedRecordWithTaxBean bean : planRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecordWithTax(bean);

            record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setBudgetPlanned_gross(bean.getValueWithTaxes());
        }
        for (WeeklyAggregatedRecordWithTaxBean bean : workRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecordWithTax(bean);

            record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setBudgetBurned_gross(bean.getValueWithTaxes());
            record.setHours(bean.getHours());
        }

        List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
        Collections.sort(records, new AggregatedRecordComparator());

        //Sum up records of the same week
        records = sumRecordsForEachPeriod(records);

        return records;
    }

    /**
     * Goes through a list of records by time periods and sums up the money of all records for each period.
     * @param records a list of records to sum up
     * @return a list of the summed records
     */
    private List<AggregatedRecord> sumRecordsForEachPeriod(List<AggregatedRecord> records)
    {
        int i = 0;
        while(i<records.size()-1){
            AggregatedRecord current = records.get(i);
            AggregatedRecord next = records.get(i + 1);

            // Create new record as a sum
            if (current.getAggregationPeriodStart().equals(next.getAggregationPeriodStart()) && current.getAggregationPeriodEnd().equals(next.getAggregationPeriodEnd())) {
                AggregatedRecord sum = new AggregatedRecord();
                sum.setAggregationPeriodTitle(current.getAggregationPeriodTitle());
                sum.setAggregationPeriodStart(current.getAggregationPeriodStart());
                sum.setAggregationPeriodEnd(current.getAggregationPeriodEnd());

                if (current.getHours() == null) {
                    sum.setHours(next.getHours());
                } else if (next.getHours() == null) {
                    sum.setHours(current.getHours());
                } else {
                    sum.setHours(current.getHours() + next.getHours());
                }

                sum.setBudgetPlanned_net(MoneyUtil.sumMoney(current.getBudgetPlanned_net(), next.getBudgetPlanned_net()));
                sum.setBudgetPlanned_gross(MoneyUtil.sumMoney(current.getBudgetPlanned_gross(), next.getBudgetPlanned_gross()));
                sum.setBudgetBurned_net(MoneyUtil.sumMoney(current.getBudgetBurned_net(), next.getBudgetBurned_net()));
                sum.setBudgetBurned_gross(MoneyUtil.sumMoney(current.getBudgetBurned_gross(), next.getBudgetBurned_gross()));

                // Replace the two old records by their sum
                records.remove(current);
                records.remove(next);
                records.add(i, sum);
            }
            else {
                // Go to the next record
                i++;
            }
        }

        return records;
    }

    public List<AggregatedRecord> joinMonthly(List<MonthlyAggregatedRecordBean> workRecords, List<MonthlyAggregatedRecordBean> planRecords) {
        RecordMap recordMap = new RecordMap();
        for (MonthlyAggregatedRecordBean bean : planRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecord(bean);
            record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
        }
        for (MonthlyAggregatedRecordBean bean : workRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecord(bean);
            record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setHours(bean.getHours());
        }
        List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
        Collections.sort(records, new AggregatedRecordComparator());
        return records;
    }

    public List<AggregatedRecord> joinMonthlyWithTax(List<MonthlyAggregatedRecordWithTaxBean> workRecords, List<MonthlyAggregatedRecordWithTaxBean> planRecords) {
        RecordMap recordMap = new RecordMap();
        for (MonthlyAggregatedRecordWithTaxBean bean : planRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecordWithTax(bean);
            record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setBudgetPlanned_gross(bean.getValueWithTaxes());
        }
        for (MonthlyAggregatedRecordWithTaxBean bean : workRecords) {
            AggregatedRecord record = recordMap.getOrCreateRecordWithTax(bean);
            record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
            record.setBudgetBurned_gross(bean.getValueWithTaxes());
            record.setHours(bean.getHours());
        }
        List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
        Collections.sort(records, new AggregatedRecordComparator());

        //Sum up records of the same month
        records = sumRecordsForEachPeriod(records);

        return records;
    }

    class RecordMap extends HashMap<String, AggregatedRecord> {

        public AggregatedRecord getOrCreateRecord(WeeklyAggregatedRecordBean recordBean) {
            AggregatedRecord record = get(getKey(recordBean));
            if (record == null) {
                Calendar c = Calendar.getInstance(Locale.GERMAN);
                c.clear();
                c.set(Calendar.YEAR, recordBean.getYear());
                c.set(Calendar.WEEK_OF_YEAR, recordBean.getWeek());
                record = new AggregatedRecord();
                record.setAggregationPeriodStart(c.getTime());
                c.add(Calendar.DAY_OF_YEAR, 6);
                record.setAggregationPeriodEnd(c.getTime());
                record.setAggregationPeriodTitle(String.format("Week #%d", recordBean.getWeek()));
                put(getKey(recordBean), record);
            }
            return record;
        }

        public AggregatedRecord getOrCreateRecordWithTax(WeeklyAggregatedRecordWithTaxBean recordBean) {
            AggregatedRecord record = get(getKey(recordBean));
            if (record == null) {
                Calendar c = Calendar.getInstance(Locale.GERMAN);
                c.clear();
                c.set(Calendar.YEAR, recordBean.getYear());
                c.set(Calendar.WEEK_OF_YEAR, recordBean.getWeek());
                record = new AggregatedRecord();
                record.setAggregationPeriodStart(c.getTime());
                c.add(Calendar.DAY_OF_YEAR, 6);
                record.setAggregationPeriodEnd(c.getTime());
                record.setAggregationPeriodTitle(String.format("Week #%d", recordBean.getWeek()));
                put(getKey(recordBean), record);
            }
            return record;
        }

        public AggregatedRecord getOrCreateRecord(MonthlyAggregatedRecordBean recordBean) {
            AggregatedRecord record = get(getKey(recordBean));
            if (record == null) {
                Calendar c = Calendar.getInstance(Locale.GERMAN);
                c.clear();
                c.set(Calendar.YEAR, recordBean.getYear());
                c.set(Calendar.MONTH, recordBean.getMonth());
                record = new AggregatedRecord();
                record.setAggregationPeriodStart(c.getTime());
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.DAY_OF_YEAR, -1);
                record.setAggregationPeriodEnd(c.getTime());
                record.setAggregationPeriodTitle(String.format("%d/%02d", recordBean.getYear(), recordBean.getMonth() + 1));
                put(getKey(recordBean), record);
            }
            return record;
        }

        public AggregatedRecord getOrCreateRecordWithTax(MonthlyAggregatedRecordWithTaxBean recordBean) {
            AggregatedRecord record = get(getKey(recordBean));
            if (record == null) {
                Calendar c = Calendar.getInstance(Locale.GERMAN);
                c.clear();
                c.set(Calendar.YEAR, recordBean.getYear());
                c.set(Calendar.MONTH, recordBean.getMonth());
                record = new AggregatedRecord();
                record.setAggregationPeriodStart(c.getTime());
                c.add(Calendar.MONTH, 1);
                c.add(Calendar.DAY_OF_YEAR, -1);
                record.setAggregationPeriodEnd(c.getTime());
                record.setAggregationPeriodTitle(String.format("%d/%02d", recordBean.getYear(), recordBean.getMonth() + 1));
                put(getKey(recordBean), record);
            }
            return record;
        }

        private String getKey(WeeklyAggregatedRecordBean recordBean) {
            return String.format("%d%d", recordBean.getYear(), recordBean.getWeek());
        }

        private String getKey(WeeklyAggregatedRecordWithTaxBean recordBean) {
            return String.format("%d%d%s", recordBean.getYear(), recordBean.getWeek(), recordBean.getTaxRate());
        }

        private String getKey(MonthlyAggregatedRecordBean recordBean) {
            return String.format("%d%d", recordBean.getYear(), recordBean.getMonth());
        }

        private String getKey(MonthlyAggregatedRecordWithTaxBean recordBean) {
            return String.format("%d%d%s", recordBean.getYear(), recordBean.getMonth(), recordBean.getTaxRate());
        }

    }
}
