package org.wickedsource.budgeteer.service.record;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class RecordJoinerTest extends ServiceTestTemplate{

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private RecordJoiner joiner;

    @Test
    void testJoinWeekly() throws Exception {
        List<AggregatedRecord> records = joiner.joinWeekly(createWeeklyWorkRecords(), createWeeklyPlanRecords());
        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(format.parse("03.03.2014"), records.get(0).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("09.03.2014"), records.get(0).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #10", records.get(0).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlannedNet());
        Assertions.assertEquals(5d, records.get(0).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("06.04.2015"), records.get(1).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("12.04.2015"), records.get(1).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #15", records.get(1).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlannedNet());
        Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("13.04.2015"), records.get(2).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("19.04.2015"), records.get(2).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #16", records.get(2).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlannedNet());
        Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);
    }

    @Test
    void testJoinWeeklyWithTax() throws Exception {
        List<AggregatedRecord> recordsWithTax = joiner.joinWeeklyWithTax(createWeeklyWorkRecordsWithTax(), createWeeklyPlanRecordsWithTax());
        Assertions.assertEquals(3, recordsWithTax.size());

        Assertions.assertEquals(format.parse("03.03.2014"), recordsWithTax.get(0).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("09.03.2014"), recordsWithTax.get(0).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #10", recordsWithTax.get(0).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(20000), recordsWithTax.get(0).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), recordsWithTax.get(0).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(22500), recordsWithTax.get(0).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(13530), recordsWithTax.get(0).getBudgetPlannedGross());
        Assertions.assertEquals(10d, recordsWithTax.get(0).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("06.04.2015"), recordsWithTax.get(1).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("12.04.2015"), recordsWithTax.get(1).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #15", recordsWithTax.get(1).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), recordsWithTax.get(1).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), recordsWithTax.get(1).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(55000), recordsWithTax.get(1).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(13530), recordsWithTax.get(1).getBudgetPlannedGross());
        Assertions.assertEquals(5d, recordsWithTax.get(1).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("13.04.2015"), recordsWithTax.get(2).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("19.04.2015"), recordsWithTax.get(2).getAggregationPeriodEnd());
        Assertions.assertEquals("Week #16", recordsWithTax.get(2).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(60000), recordsWithTax.get(2).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000), recordsWithTax.get(2).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(66000), recordsWithTax.get(2).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(112500), recordsWithTax.get(2).getBudgetPlannedGross());
        Assertions.assertEquals(6d, recordsWithTax.get(2).getHours(), 0.1d);
    }

    @Test
    void testJoinMonthly() throws Exception {
        List<AggregatedRecord> records = joiner.joinMonthly(createMonthlyWorkRecords(), createMonthlyPlanRecords());
        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(format.parse("01.01.2014"), records.get(0).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("31.01.2014"), records.get(0).getAggregationPeriodEnd());
        Assertions.assertEquals("2014/01", records.get(0).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlannedNet());
        Assertions.assertEquals(5d, records.get(0).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("01.06.2015"), records.get(1).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("30.06.2015"), records.get(1).getAggregationPeriodEnd());
        Assertions.assertEquals("2015/06", records.get(1).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlannedNet());
        Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("01.07.2015"), records.get(2).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("31.07.2015"), records.get(2).getAggregationPeriodEnd());
        Assertions.assertEquals("2015/07", records.get(2).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlannedNet());
        Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);

    }

    @Test
    void testJoinMonthlyWithTax() throws Exception {
        List<AggregatedRecord> records = joiner.joinMonthlyWithTax(createMonthlyWorkRecordsWithTax(), createMonthlyPlanRecordsWithTax());
        Assertions.assertEquals(3, records.size());

        Assertions.assertEquals(format.parse("01.01.2014"), records.get(0).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("31.01.2014"), records.get(0).getAggregationPeriodEnd());
        Assertions.assertEquals("2014/01", records.get(0).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000), records.get(0).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(107500), records.get(0).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(13530), records.get(0).getBudgetPlannedGross());
        Assertions.assertEquals(10d, records.get(0).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("01.06.2015"), records.get(1).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("30.06.2015"), records.get(1).getAggregationPeriodEnd());
        Assertions.assertEquals("2015/06", records.get(1).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(55000), records.get(1).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(13530), records.get(1).getBudgetPlannedGross());
        Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

        Assertions.assertEquals(format.parse("01.07.2015"), records.get(2).getAggregationPeriodStart());
        Assertions.assertEquals(format.parse("31.07.2015"), records.get(2).getAggregationPeriodEnd());
        Assertions.assertEquals("2015/07", records.get(2).getAggregationPeriodTitle());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurnedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(20000), records.get(2).getBudgetPlannedNet());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(66000), records.get(2).getBudgetBurnedGross());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(21500), records.get(2).getBudgetPlannedGross());
        Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);
    }

    private List<WeeklyAggregatedRecordBean> createWeeklyWorkRecords() {
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<WeeklyAggregatedRecordBean>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 50000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 60000));
        beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 50000));
        return beans;
    }

    private List<WeeklyAggregatedRecordWithTaxBean> createWeeklyWorkRecordsWithTax(){
        List<WeeklyAggregatedRecordWithTaxBean> beans = new ArrayList<WeeklyAggregatedRecordWithTaxBean>();
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2015, 15, 5d, 50000, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2015, 16, 6d, 60000, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2014, 10, 5d, 10000, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2014, 10, 5d, 10000, BigDecimal.valueOf(15)));
        return beans;
    }

    private List<WeeklyAggregatedRecordBean> createWeeklyPlanRecords(){
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<WeeklyAggregatedRecordBean>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 12300));
        beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 12300));
        beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 32100));
        return beans;
    }

    private List<WeeklyAggregatedRecordWithTaxBean> createWeeklyPlanRecordsWithTax(){
        List<WeeklyAggregatedRecordWithTaxBean> beans = new ArrayList<WeeklyAggregatedRecordWithTaxBean>();
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2015, 15, 5d, 12300, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2014, 10, 5d, 12300, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2015, 16, 6d, 50000, BigDecimal.TEN));
        beans.add(new WeeklyAggregatedRecordWithTaxBean(2015, 16, 6d, 50000, BigDecimal.valueOf(15)));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createMonthlyWorkRecords() {
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 50000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 60000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 50000));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createMonthlyPlanRecords(){
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 12300));
        beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 12300));
        beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 32100));
        return beans;
    }

    private List<MonthlyAggregatedRecordWithTaxBean> createMonthlyWorkRecordsWithTax() {
        List<MonthlyAggregatedRecordWithTaxBean> beans = new ArrayList<MonthlyAggregatedRecordWithTaxBean>();
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 5, 5d, 50000, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 60000, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 50000, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 50000, BigDecimal.valueOf(5)));
        return beans;
    }

    private List<MonthlyAggregatedRecordWithTaxBean> createMonthlyPlanRecordsWithTax(){
        List<MonthlyAggregatedRecordWithTaxBean> beans = new ArrayList<MonthlyAggregatedRecordWithTaxBean>();
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 5, 5d, 12300, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 12300, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 10000, BigDecimal.valueOf(10)));
        beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 10000, BigDecimal.valueOf(5)));
        return beans;
    }


}
