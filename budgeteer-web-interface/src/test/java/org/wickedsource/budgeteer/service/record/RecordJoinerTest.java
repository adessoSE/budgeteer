package org.wickedsource.budgeteer.service.record;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.record.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.statistics.MonthlyStats;

class RecordJoinerTest extends ServiceTestTemplate {

  private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

  @Autowired private RecordJoiner joiner;

  @Test
  void testJoinWeekly() throws Exception {
    List<AggregatedRecord> records =
        joiner.joinWeekly(createWeeklyWorkRecords(), createWeeklyPlanRecords());
    Assertions.assertEquals(3, records.size());

    Assertions.assertEquals(format.parse("03.03.2014"), records.get(0).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("09.03.2014"), records.get(0).getAggregationPeriodEnd());
    Assertions.assertEquals("Week 2014-10", records.get(0).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlanned_net());
    Assertions.assertEquals(5d, records.get(0).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("06.04.2015"), records.get(1).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("12.04.2015"), records.get(1).getAggregationPeriodEnd());
    Assertions.assertEquals("Week 2015-15", records.get(1).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlanned_net());
    Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("13.04.2015"), records.get(2).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("19.04.2015"), records.get(2).getAggregationPeriodEnd());
    Assertions.assertEquals("Week 2015-16", records.get(2).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlanned_net());
    Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);
  }

  @Test
  void testJoinWeeklyWithTax() throws Exception {
    List<AggregatedRecord> recordsWithTax =
        joiner.joinWeeklyByMonthFraction(
            createWeeklyWorkRecordsWithTax(),
            createWeeklyPlanRecordsWithTax(),
            createMonthlyStats());
    Assertions.assertEquals(2, recordsWithTax.size());

    Assertions.assertEquals(
        format.parse("26.03.2018"), recordsWithTax.get(0).getAggregationPeriodStart());
    Assertions.assertEquals(
        format.parse("1.04.2018"), recordsWithTax.get(0).getAggregationPeriodEnd());
    Assertions.assertEquals("Week 2018-13", recordsWithTax.get(0).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(16250), recordsWithTax.get(0).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(18750), recordsWithTax.get(0).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(17625), recordsWithTax.get(0).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(20313), recordsWithTax.get(0).getBudgetPlanned_gross());
    Assertions.assertEquals(13d, recordsWithTax.get(0).getHours(), 0.1d);

    Assertions.assertEquals(
        format.parse("02.04.2018"), recordsWithTax.get(1).getAggregationPeriodStart());
    Assertions.assertEquals(
        format.parse("08.04.2018"), recordsWithTax.get(1).getAggregationPeriodEnd());
    Assertions.assertEquals("Week 2018-14", recordsWithTax.get(1).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(3750), recordsWithTax.get(1).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(5000), recordsWithTax.get(1).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(4125), recordsWithTax.get(1).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(5500), recordsWithTax.get(1).getBudgetPlanned_gross());
    Assertions.assertEquals(3d, recordsWithTax.get(1).getHours(), 0.1d);
  }

  @Test
  void testJoinMonthly() throws Exception {
    List<AggregatedRecord> records =
        joiner.joinMonthly(createMonthlyWorkRecords(), createMonthlyPlanRecords());
    Assertions.assertEquals(3, records.size());

    Assertions.assertEquals(format.parse("01.01.2014"), records.get(0).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.01.2014"), records.get(0).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2014-01", records.get(0).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlanned_net());
    Assertions.assertEquals(5d, records.get(0).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.06.2015"), records.get(1).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("30.06.2015"), records.get(1).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2015-06", records.get(1).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlanned_net());
    Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.07.2015"), records.get(2).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.07.2015"), records.get(2).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2015-07", records.get(2).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlanned_net());
    Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);
  }

  @Test
  void testJoinMonthlyWithTax() throws Exception {
    List<AggregatedRecord> records =
        joiner.joinMonthlyWithTax(
            createMonthlyWorkRecordsWithTax(), createMonthlyPlanRecordsWithTax());
    Assertions.assertEquals(6, records.size());

    Assertions.assertEquals(format.parse("01.01.2014"), records.get(0).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.01.2014"), records.get(0).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2014-01", records.get(0).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(100000), records.get(0).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(107500), records.get(0).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(13530), records.get(0).getBudgetPlanned_gross());
    Assertions.assertEquals(10d, records.get(0).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.06.2015"), records.get(1).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("30.06.2015"), records.get(1).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2015-06", records.get(1).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(55000), records.get(1).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(13530), records.get(1).getBudgetPlanned_gross());
    Assertions.assertEquals(5d, records.get(1).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.07.2015"), records.get(2).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.07.2015"), records.get(2).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2015-07", records.get(2).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(20000), records.get(2).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(66000), records.get(2).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(21500), records.get(2).getBudgetPlanned_gross());
    Assertions.assertEquals(6d, records.get(2).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.01.2016"), records.get(3).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.01.2016"), records.get(3).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2016-01", records.get(3).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(143750), records.get(3).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(156250), records.get(3).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(152219), records.get(3).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(166157), records.get(3).getBudgetPlanned_gross());
    Assertions.assertEquals(115d, records.get(3).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.06.2016"), records.get(4).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("30.06.2016"), records.get(4).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2016-06", records.get(4).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(201250), records.get(4).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(208125), records.get(4).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(221375), records.get(4).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(228938), records.get(4).getBudgetPlanned_gross());
    Assertions.assertEquals(161d, records.get(4).getHours(), 0.1d);

    Assertions.assertEquals(format.parse("01.07.2016"), records.get(5).getAggregationPeriodStart());
    Assertions.assertEquals(format.parse("31.07.2016"), records.get(5).getAggregationPeriodEnd());
    Assertions.assertEquals("Month 2016-07", records.get(5).getAggregationPeriodTitle());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(200000), records.get(5).getBudgetBurned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(208125), records.get(5).getBudgetPlanned_net());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(220000), records.get(5).getBudgetBurned_gross());
    Assertions.assertEquals(
        MoneyUtil.createMoneyFromCents(228938), records.get(5).getBudgetPlanned_gross());
    Assertions.assertEquals(160d, records.get(5).getHours(), 0.1d);
  }

  private List<WeeklyAggregatedRecordBean> createWeeklyWorkRecords() {
    List<WeeklyAggregatedRecordBean> beans = new ArrayList<>();
    beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 50000));
    beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 60000));
    beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 50000));
    return beans;
  }

  private List<WeeklyAggregatedRecordWithTitleAndTaxBean> createWeeklyWorkRecordsWithTax() {
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> beans = new ArrayList<>();
    beans.add(
        new WeeklyAggregatedRecordWithTitleAndTaxBean(
            2018,
            3,
            13,
            180,
            MoneyUtil.createMoneyFromCents(10000),
            BigDecimal.TEN,
            "Max Mustermann"));
    beans.add(
        new WeeklyAggregatedRecordWithTitleAndTaxBean(
            2018,
            3,
            13,
            180,
            MoneyUtil.createMoneyFromCents(10000),
            BigDecimal.TEN,
            "Maria Mustermann"));
    beans.add(
        new WeeklyAggregatedRecordWithTitleAndTaxBean(
            2018,
            4,
            13,
            180,
            MoneyUtil.createMoneyFromCents(10000),
            BigDecimal.TEN,
            "Max Mustermann"));
    beans.add(
        new WeeklyAggregatedRecordWithTitleAndTaxBean(
            2018,
            4,
            13,
            240,
            MoneyUtil.createMoneyFromCents(10000),
            BigDecimal.valueOf(5),
            "Max Mustermann"));
    beans.add(
        new WeeklyAggregatedRecordWithTitleAndTaxBean(
            2018,
            4,
            14,
            180,
            MoneyUtil.createMoneyFromCents(10000),
            BigDecimal.TEN,
            "Max Mustermann"));
    return beans;
  }

  private List<WeeklyAggregatedRecordBean> createWeeklyPlanRecords() {
    List<WeeklyAggregatedRecordBean> beans = new ArrayList<>();
    beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 12300));
    beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 12300));
    beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 32100));
    return beans;
  }

  private List<WeeklyAggregatedRecordWithTaxBean> createWeeklyPlanRecordsWithTax() {
    List<WeeklyAggregatedRecordWithTaxBean> beans = new ArrayList<>();
    beans.add(
        new WeeklyAggregatedRecordWithTaxBean(
            2018, 3, 13, 300, MoneyUtil.createMoneyFromCents(10000), BigDecimal.TEN));
    beans.add(
        new WeeklyAggregatedRecordWithTaxBean(
            2018, 4, 13, 300, MoneyUtil.createMoneyFromCents(10000), BigDecimal.TEN));
    beans.add(
        new WeeklyAggregatedRecordWithTaxBean(
            2018, 4, 13, 300, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(5)));
    beans.add(
        new WeeklyAggregatedRecordWithTaxBean(
            2018, 4, 14, 240, MoneyUtil.createMoneyFromCents(10000), BigDecimal.TEN));
    return beans;
  }

  private MonthlyStats createMonthlyStats() {
    List<MonthlyAggregatedRecordWithTaxBean> planStats = new ArrayList<>();
    planStats.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2018, 3, 300, MoneyUtil.createMoneyFromCents(10000), BigDecimal.TEN));
    planStats.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2018, 4, 540, MoneyUtil.createMoneyFromCents(10000), BigDecimal.TEN));
    planStats.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2018, 4, 300, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(5)));

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> workStats = new ArrayList<>();
    workStats.add(
        new MonthlyAggregatedRecordWithTitleAndTaxBean(
            2018, 3, 180, MoneyUtil.createMoneyFromCents(10000), "Max Mustermann", BigDecimal.TEN));
    workStats.add(
        new MonthlyAggregatedRecordWithTitleAndTaxBean(
            2018,
            3,
            180,
            MoneyUtil.createMoneyFromCents(10000),
            "Maria Mustermann",
            BigDecimal.TEN));
    workStats.add(
        new MonthlyAggregatedRecordWithTitleAndTaxBean(
            2018, 4, 360, MoneyUtil.createMoneyFromCents(10000), "Max Mustermann", BigDecimal.TEN));
    workStats.add(
        new MonthlyAggregatedRecordWithTitleAndTaxBean(
            2018,
            4,
            240,
            MoneyUtil.createMoneyFromCents(10000),
            "Max Mustermann",
            BigDecimal.valueOf(5)));

    return new MonthlyStats(planStats, workStats);
  }

  private List<MonthlyAggregatedRecordBean> createMonthlyWorkRecords() {
    List<MonthlyAggregatedRecordBean> beans = new ArrayList<>();
    beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 50000));
    beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 60000));
    beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 50000));
    return beans;
  }

  private List<MonthlyAggregatedRecordBean> createMonthlyPlanRecords() {
    List<MonthlyAggregatedRecordBean> beans = new ArrayList<>();
    beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 12300));
    beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 12300));
    beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 32100));
    return beans;
  }

  private List<MonthlyAggregatedRecordWithTaxBean> createMonthlyWorkRecordsWithTax() {
    List<MonthlyAggregatedRecordWithTaxBean> beans = new ArrayList<>();
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 5, 5d, 50000, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 60000, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 50000, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 50000, BigDecimal.valueOf(5)));

    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 5, 9656, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 6, 9600, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 0, 1234, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 0, 5678, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(5)));

    return beans;
  }

  private List<MonthlyAggregatedRecordWithTaxBean> createMonthlyPlanRecordsWithTax() {
    List<MonthlyAggregatedRecordWithTaxBean> beans = new ArrayList<>();
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 5, 5d, 12300, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2014, 0, 5d, 12300, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 10000, BigDecimal.valueOf(10)));
    beans.add(new MonthlyAggregatedRecordWithTaxBean(2015, 6, 6d, 10000, BigDecimal.valueOf(5)));

    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 5, 10000, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 6, 10000, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 0, 2000, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(10)));
    beans.add(
        new MonthlyAggregatedRecordWithTaxBean(
            2016, 0, 5500, MoneyUtil.createMoneyFromCents(10000), BigDecimal.valueOf(5)));
    return beans;
  }
}
