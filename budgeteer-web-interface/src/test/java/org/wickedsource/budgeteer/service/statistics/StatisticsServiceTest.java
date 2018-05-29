package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.service.DateProvider;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.*;

class StatisticsServiceTest extends ServiceTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private StatisticsService service;

    private static final Comparator<MoneySeries> moneySeriesComparator = new Comparator<MoneySeries>() {
        @Override
        public int compare(MoneySeries o1, MoneySeries o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    @Test
    void testGetWeeklyBudgetBurnedForProject() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekForProject(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetBurnedForProject(1L, 5);
        Assertions.assertEquals(5, resultList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), resultList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), resultList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), resultList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), resultList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), resultList.get(4));
    }

    @Test
    void testGetWeeklyBudgetPlannedForProject() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(planRecordRepository.aggregateByWeekForProject(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetPlannedForProject(1L, 5);
        Assertions.assertEquals(5, resultList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), resultList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), resultList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), resultList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), resultList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), resultList.get(4));
    }

    @Test
    void testGetWeeklyBudgetBurnedForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetBurnedForPerson(1L, 5);
        Assertions.assertEquals(5, resultList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), resultList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), resultList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), resultList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), resultList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), resultList.get(4));
    }

    @Test
    void testGetWeeklyBudgetPlannedForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(planRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetPlannedForPerson(1L, 5);
        Assertions.assertEquals(5, resultList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), resultList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), resultList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), resultList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), resultList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), resultList.get(4));
    }

    private List<WeeklyAggregatedRecordBean> createLast5Weeks() {
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 1, 15d, 100000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 2, 15d, 200000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 4, 15d, 400000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 5, 15d, 500000));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createLast5Months() {
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<>();
        beans.add(new MonthlyAggregatedRecordBean(2014, 8, 15d, 100000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 9, 15d, 200000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 11, 15d, 400000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 0, 15d, 500000));
        return beans;
    }


    @Test
    void testGetAvgDailyRateForPreviousDays() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("05.01.2015"));
        when(workRecordRepository.getAverageDailyRatesPerDay(anyLong(), any(Date.class))).thenReturn(createLast5Days());
        List<Money> resultList = service.getAvgDailyRateForPreviousDays(1L, 5);
        Assertions.assertEquals(5, resultList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100L), resultList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200L), resultList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), resultList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400L), resultList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500L), resultList.get(4));
    }

    private List<DailyAverageRateBean> createLast5Days() {
        List<DailyAverageRateBean> beans = new ArrayList<>();
        beans.add(new DailyAverageRateBean(2015, 0, 1, 100d));
        beans.add(new DailyAverageRateBean(2015, 0, 2, 200d));
        beans.add(new DailyAverageRateBean(2015, 0, 4, 400d));
        beans.add(new DailyAverageRateBean(2015, 0, 5, 500d));
        return beans;
    }

    @Test
    void testGetBudgetDistribution() throws Exception {
        when(workRecordRepository.getBudgetShareForPerson(1L)).thenReturn(createShares());
        List<Share> shares = service.getBudgetDistribution(1L);
        Assertions.assertEquals(4, shares.size());
        Assertions.assertEquals("share1", shares.get(0).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(10000L), shares.get(0).getShare());
        Assertions.assertEquals("share2", shares.get(1).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(20000L), shares.get(1).getShare());
        Assertions.assertEquals("share3", shares.get(2).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(30000L), shares.get(2).getShare());
        Assertions.assertEquals("share4", shares.get(3).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(40000L), shares.get(3).getShare());
    }

    private List<ShareBean> createShares() {
        List<ShareBean> shares = new ArrayList<>();
        shares.add(new ShareBean("share1", 10000L));
        shares.add(new ShareBean("share2", 20000L));
        shares.add(new ShareBean("share3", 30000L));
        shares.add(new ShareBean("share4", 40000L));
        return shares;
    }

    @Test
    void testGetPeopleDistribution() throws Exception {
        when(workRecordRepository.getPersonShareForBudget(1L)).thenReturn(createShares());
        List<Share> shares = service.getPeopleDistribution(1L);
        Assertions.assertEquals(4, shares.size());
        Assertions.assertEquals("share1", shares.get(0).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(10000L), shares.get(0).getShare());
        Assertions.assertEquals("share2", shares.get(1).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(20000L), shares.get(1).getShare());
        Assertions.assertEquals("share3", shares.get(2).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(30000L), shares.get(2).getShare());
        Assertions.assertEquals("share4", shares.get(3).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(40000L), shares.get(3).getShare());
    }

    @Test
    void testGetWeekStatsForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndBudgetForPerson(anyLong(), any(Date.class))).thenReturn(createLast5WeeksForBudget());
        when(planRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForPerson(1L, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Budget 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Budget 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }

    @Test
    void testGetWeekStatsForBudget() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndPersonForBudget(anyLong(), any(Date.class))).thenReturn(createLast5WeeksForPerson());
        when(planRecordRepository.aggregateByWeekForBudget(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForBudget(1L, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Person 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Person 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }

    @Test
    void testGetWeekStatsForBudgets() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndPersonForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5WeeksForBudget());
        when(planRecordRepository.aggregateByWeekForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForBudgets(new BudgetTagFilter(Arrays.asList("tag1"), 1L), 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Budget 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Budget 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }

    private List<WeeklyAggregatedRecordWithTitleBean> createLast5WeeksForBudget() {
        List<WeeklyAggregatedRecordWithTitleBean> beans = new ArrayList<>();
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 1, 15d, 100000, "Budget 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 2, 15d, 200000, "Budget 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 4, 15d, 400000, "Budget 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 5, 15d, 500000, "Budget 1"));

        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 1, 15d, 100000, "Budget 2"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 2, 15d, 200000, "Budget 2"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 5, 15d, 500000, "Budget 2"));
        return beans;
    }

    private List<MonthlyAggregatedRecordWithTitleBean> createLast5MonthsForBudget() {
        List<MonthlyAggregatedRecordWithTitleBean> beans = new ArrayList<>();
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2014, 8, 15d, 100000, "Budget 1"));
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2014, 9, 15d, 200000, "Budget 1"));
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2014, 11, 15d, 400000, "Budget 1"));
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2015, 0, 15d, 500000, "Budget 1"));

        beans.add(new MonthlyAggregatedRecordWithTitleBean(2014, 8, 15d, 100000, "Budget 2"));
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2014, 9, 15d, 200000, "Budget 2"));
        beans.add(new MonthlyAggregatedRecordWithTitleBean(2015, 0, 15d, 500000, "Budget 2"));
        return beans;
    }

    private List<WeeklyAggregatedRecordWithTitleBean> createLast5WeeksForPerson() {
        List<WeeklyAggregatedRecordWithTitleBean> beans = new ArrayList<>();
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 1, 15d, 100000, "Person 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 2, 15d, 200000, "Person 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 4, 15d, 400000, "Person 1"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 5, 15d, 500000, "Person 1"));

        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 1, 15d, 100000, "Person 2"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 2, 15d, 200000, "Person 2"));
        beans.add(new WeeklyAggregatedRecordWithTitleBean(2015, 5, 15d, 500000, "Person 2"));
        return beans;
    }

    @Test
    void testGetMonthStatsForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndBudgetForPerson(anyLong(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForPerson(1L, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Budget 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Budget 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }

    @Test
    void testGetMonthStatsForBudgets() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndPersonForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForBudgets(new BudgetTagFilter(Arrays.asList("tag1"), 1L), 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Budget 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Budget 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }

    @Test
    void testGetMonthStatsForBudget() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndPersonForBudget(anyLong(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForBudget(anyLong(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForBudget(1L, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assertions.assertEquals(5, targetSeries.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), targetSeries.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), targetSeries.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), targetSeries.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), targetSeries.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), targetSeries.get(4));

        Assertions.assertEquals(2, targetAndActual.getActualSeries().size());
        targetAndActual.getActualSeries().sort(moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assertions.assertEquals("Budget 2", actualSeries1.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries1.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries1.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries1.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assertions.assertEquals("Budget 1", actualSeries2.getName());
        Assertions.assertEquals(5, actualSeries1.getValues().size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000L), actualSeries2.getValues().get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000L), actualSeries2.getValues().get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0L), actualSeries2.getValues().get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000L), actualSeries2.getValues().get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(500000L), actualSeries2.getValues().get(4));
    }


    @Test
    void testFillMissingMonths() throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        List<MonthlyAggregatedRecordBean> beans = new LinkedList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2015, 1, 15d, 100000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 2, 15d, 200000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 4, 15d, 400000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 5, 15d, 500000));

        LinkedList<Money> testList = new LinkedList<Money>();
        DateUtil dateUtil = Mockito.mock(DateUtil.class);
        when(dateProvider.currentDate()).thenReturn(format.parse("01.05.2015"));
        service.fillMissingMonths(5, beans, testList);

        Assertions.assertEquals(5, testList.size());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0), testList.get(0));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(100000), testList.get(1));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(200000), testList.get(2));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(0), testList.get(3));
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400000), testList.get(4));

    }

}
