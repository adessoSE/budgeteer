package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.service.DateProvider;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.*;

public class StatisticsServiceTest extends ServiceTestTemplate {

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
    public void testGetWeeklyBudgetBurnedForProject() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekForProject(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetBurnedForProject(1l, 5);
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), resultList.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), resultList.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), resultList.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), resultList.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), resultList.get(4));
    }

    @Test
    public void testGetWeeklyBudgetPlannedForProject() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(planRecordRepository.aggregateByWeekForProject(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetPlannedForProject(1l, 5);
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), resultList.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), resultList.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), resultList.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), resultList.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), resultList.get(4));
    }

    @Test
    public void testGetWeeklyBudgetBurnedForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetBurnedForPerson(1l, 5);
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), resultList.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), resultList.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), resultList.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), resultList.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), resultList.get(4));
    }

    @Test
    public void testGetWeeklyBudgetPlannedForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(planRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        List<Money> resultList = service.getWeeklyBudgetPlannedForPerson(1l, 5);
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), resultList.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), resultList.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), resultList.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), resultList.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), resultList.get(4));
    }

    private List<WeeklyAggregatedRecordBean> createLast5Weeks() {
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<WeeklyAggregatedRecordBean>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 1, 15d, 100000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 2, 15d, 200000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 4, 15d, 400000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 5, 15d, 500000));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createLast5Months() {
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2014, 8, 15d, 100000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 9, 15d, 200000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 11, 15d, 400000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 0, 15d, 500000));
        return beans;
    }


    @Test
    public void testGetAvgDailyRateForPreviousDays() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("05.01.2015"));
        when(workRecordRepository.getAverageDailyRatesPerDay(anyLong(), any(Date.class))).thenReturn(createLast5Days());
        List<Money> resultList = service.getAvgDailyRateForPreviousDays(1l, 5);
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100l), resultList.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200l), resultList.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), resultList.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400l), resultList.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500l), resultList.get(4));
    }

    private List<DailyAverageRateBean> createLast5Days() {
        List<DailyAverageRateBean> beans = new ArrayList<DailyAverageRateBean>();
        beans.add(new DailyAverageRateBean(2015, 0, 1, 100d));
        beans.add(new DailyAverageRateBean(2015, 0, 2, 200d));
        beans.add(new DailyAverageRateBean(2015, 0, 4, 400d));
        beans.add(new DailyAverageRateBean(2015, 0, 5, 500d));
        return beans;
    }

    @Test
    public void testGetBudgetDistribution() throws Exception {
        when(workRecordRepository.getBudgetShareForPerson(1l)).thenReturn(createShares());
        List<Share> shares = service.getBudgetDistribution(1l);
        Assert.assertEquals(4, shares.size());
        Assert.assertEquals("share1", shares.get(0).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), shares.get(0).getShare());
        Assert.assertEquals("share2", shares.get(1).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(20000l), shares.get(1).getShare());
        Assert.assertEquals("share3", shares.get(2).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(30000l), shares.get(2).getShare());
        Assert.assertEquals("share4", shares.get(3).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(40000l), shares.get(3).getShare());
    }

    private List<ShareBean> createShares() {
        List<ShareBean> shares = new ArrayList<ShareBean>();
        shares.add(new ShareBean("share1", 10000l));
        shares.add(new ShareBean("share2", 20000l));
        shares.add(new ShareBean("share3", 30000l));
        shares.add(new ShareBean("share4", 40000l));
        return shares;
    }

    @Test
    public void testGetPeopleDistribution() throws Exception {
        when(workRecordRepository.getPersonShareForBudget(1l)).thenReturn(createShares());
        List<Share> shares = service.getPeopleDistribution(1l);
        Assert.assertEquals(4, shares.size());
        Assert.assertEquals("share1", shares.get(0).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), shares.get(0).getShare());
        Assert.assertEquals("share2", shares.get(1).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(20000l), shares.get(1).getShare());
        Assert.assertEquals("share3", shares.get(2).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(30000l), shares.get(2).getShare());
        Assert.assertEquals("share4", shares.get(3).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(40000l), shares.get(3).getShare());
    }

    @Test
    public void testGetWeekStatsForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndBudgetForPerson(anyLong(), any(Date.class))).thenReturn(createLast5WeeksForBudget());
        when(planRecordRepository.aggregateByWeekForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForPerson(1l, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Budget 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Budget 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }

    @Test
    public void testGetWeekStatsForBudget() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndPersonForBudget(anyLong(), any(Date.class))).thenReturn(createLast5WeeksForPerson());
        when(planRecordRepository.aggregateByWeekForBudget(anyLong(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForBudget(1l, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Person 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Person 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }

    @Test
    public void testGetWeekStatsForBudgets() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByWeekAndPersonForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5WeeksForBudget());
        when(planRecordRepository.aggregateByWeekForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5Weeks());
        TargetAndActual targetAndActual = service.getWeekStatsForBudgets(new BudgetTagFilter(Arrays.asList("tag1"), 1l), 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Budget 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Budget 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }

    private List<WeeklyAggregatedRecordWithTitleBean> createLast5WeeksForBudget() {
        List<WeeklyAggregatedRecordWithTitleBean> beans = new ArrayList<WeeklyAggregatedRecordWithTitleBean>();
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
        List<MonthlyAggregatedRecordWithTitleBean> beans = new ArrayList<MonthlyAggregatedRecordWithTitleBean>();
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
        List<WeeklyAggregatedRecordWithTitleBean> beans = new ArrayList<WeeklyAggregatedRecordWithTitleBean>();
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
    public void testGetMonthStatsForPerson() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndBudgetForPerson(anyLong(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForPerson(anyLong(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForPerson(1l, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Budget 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Budget 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }

    @Test
    public void testGetMonthStatsForBudgets() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndPersonForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForBudgets(anyLong(), anyList(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForBudgets(new BudgetTagFilter(Arrays.asList("tag1"), 1l), 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Budget 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Budget 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }

    @Test
    public void testGetMonthStatsForBudget() throws Exception {
        when(dateProvider.currentDate()).thenReturn(format.parse("29.01.2015"));
        when(workRecordRepository.aggregateByMonthAndPersonForBudget(anyLong(), any(Date.class))).thenReturn(createLast5MonthsForBudget());
        when(planRecordRepository.aggregateByMonthForBudget(anyLong(), any(Date.class))).thenReturn(createLast5Months());
        TargetAndActual targetAndActual = service.getMonthStatsForBudget(1l, 5);

        List<Money> targetSeries = targetAndActual.getTargetSeries().getValues();
        Assert.assertEquals(5, targetSeries.size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), targetSeries.get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), targetSeries.get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), targetSeries.get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), targetSeries.get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), targetSeries.get(4));

        Assert.assertEquals(2, targetAndActual.getActualSeries().size());
        Collections.sort(targetAndActual.getActualSeries(), moneySeriesComparator);

        MoneySeries actualSeries1 = targetAndActual.getActualSeries().get(1);
        Assert.assertEquals("Budget 2", actualSeries1.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries1.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries1.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries1.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries1.getValues().get(4));

        MoneySeries actualSeries2 = targetAndActual.getActualSeries().get(0);
        Assert.assertEquals("Budget 1", actualSeries2.getName());
        Assert.assertEquals(5, actualSeries1.getValues().size());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(100000l), actualSeries2.getValues().get(0));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(200000l), actualSeries2.getValues().get(1));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(0l), actualSeries2.getValues().get(2));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(400000l), actualSeries2.getValues().get(3));
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(500000l), actualSeries2.getValues().get(4));
    }
}
