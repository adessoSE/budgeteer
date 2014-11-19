package org.wickedsource.budgeteer.persistence.record;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class PlanRecordRepositoryTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private PlanRecordRepository repository;

    @Test
    @DatabaseSetup("getPlannedBudget.xml")
    @DatabaseTearDown(value = "getPlannedBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetPlannedBudget() throws Exception {
        double value = repository.getPlannedBudget(1l);
        Assert.assertEquals(170000d, value, 1d);
    }

    @Test
    @DatabaseSetup("updateDailyRates.xml")
    @DatabaseTearDown(value = "updateDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdatePlanRecordDailyRates() throws Exception {
        repository.updateDailyRates(1l, 1l, format.parse("01.01.2015"), format.parse("15.08.2015"), MoneyUtil.createMoneyFromCents(50000l));
        PlanRecordEntity record = repository.findOne(1l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
        record = repository.findOne(3l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
    }

    @Test
    @DatabaseSetup("aggregateByWeekAndPerson.xml")
    @DatabaseTearDown(value = "aggregateByWeekAndPerson.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekAndPerson() {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekAndPerson(1l);
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(1, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        Assert.assertEquals(2015, records.get(2).getYear());
        Assert.assertEquals(33, records.get(2).getWeek());
        Assert.assertEquals(16d, records.get(2).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(2).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndPerson.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndPerson.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthAndPerson() {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthAndPerson(1l);
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        Assert.assertEquals(2015, records.get(2).getYear());
        Assert.assertEquals(7, records.get(2).getMonth());
        Assert.assertEquals(16d, records.get(2).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(2).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByWeekAndBudget.xml")
    @DatabaseTearDown(value = "aggregateByWeekAndBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekAndBudget() {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekAndBudget(1l);
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(1, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        Assert.assertEquals(2015, records.get(2).getYear());
        Assert.assertEquals(33, records.get(2).getWeek());
        Assert.assertEquals(16d, records.get(2).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(2).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndBudget.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthAndBudget() {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthAndBudget(1l);
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        Assert.assertEquals(2015, records.get(2).getYear());
        Assert.assertEquals(7, records.get(2).getMonth());
        Assert.assertEquals(16d, records.get(2).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(2).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByBudgetTags.xml")
    @DatabaseTearDown(value = "aggregateByBudgetTags.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregatedByWeekAndBudgetTags() {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekAndBudgetTags(1l, Arrays.asList("tag1"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(1, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        records = repository.aggregateByWeekAndBudgetTags(1l, Arrays.asList("tag2"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(16d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(33, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(1).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByBudgetTags.xml")
    @DatabaseTearDown(value = "aggregateByBudgetTags.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregatedByMonthAndBudgetTags() {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthAndBudgetTags(1l, Arrays.asList("tag1"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2014, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(1).getValueInCents());

        records = repository.aggregateByMonthAndBudgetTags(1l, Arrays.asList("tag2"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(16d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(7, records.get(1).getMonth());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(1).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekForProject() throws ParseException {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekForProject(1l, format.parse("02.01.2015"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(20000l, records.get(0).getValueInCents());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(33, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(1).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekForPerson() throws ParseException {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekForPerson(1l, format.parse("02.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(20000l, records.get(0).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekForBudget() throws ParseException {
        List<WeeklyAggregatedRecordBean> records = repository.aggregateByWeekForBudget(1l, format.parse("01.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekAndBudgetForPerson() throws ParseException {
        List<WeeklyAggregatedRecordWithTitleBean> records = repository.aggregateByWeekAndBudgetForPerson(1l, format.parse("01.01.2015"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
        Assert.assertEquals("Budget 1", records.get(0).getTitle());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(1, records.get(1).getWeek());
        Assert.assertEquals(8d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(20000l, records.get(1).getValueInCents());
        Assert.assertEquals("Budget 2", records.get(1).getTitle());
    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekAndPersonForBudget() throws ParseException {
        List<WeeklyAggregatedRecordWithTitleBean> records = repository.aggregateByWeekAndPersonForBudget(1l, format.parse("01.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
        Assert.assertEquals("person1", records.get(0).getTitle());

    }

    @Test
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByWeekAndPersonForBudgets() throws ParseException {
        List<WeeklyAggregatedRecordWithTitleBean> records = repository.aggregateByWeekAndPersonForBudgets(1l, Arrays.asList("tag1"), format.parse("01.01.2015"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(1, records.get(0).getWeek());
        Assert.assertEquals(16d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(0).getValueInCents());
        Assert.assertEquals("person1", records.get(0).getTitle());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(33, records.get(1).getWeek());
        Assert.assertEquals(16d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(1).getValueInCents());
        Assert.assertEquals("person2", records.get(1).getTitle());

    }

    @Test
    @DatabaseSetup("aggregateByMonthAndBudgetForPerson.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndBudgetForPerson.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthAndBudgetForPerson() throws ParseException {
        List<MonthlyAggregatedRecordWithTitleBean> records = repository.aggregateByMonthAndBudgetForPerson(1l, format.parse("01.01.2015"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
        Assert.assertEquals("Budget 1", records.get(0).getTitle());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(8d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(20000l, records.get(1).getValueInCents());
        Assert.assertEquals("Budget 2", records.get(1).getTitle());
    }

    @Test
    @DatabaseSetup("aggregateByMonthForPerson.xml")
    @DatabaseTearDown(value = "aggregateByMonthForPerson.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthForPerson() throws ParseException {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthForPerson(1l, format.parse("01.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(16d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(30000l, records.get(0).getValueInCents());
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndPersonForBudget.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndPersonForBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthAndPersonForBudget() throws ParseException {
        List<MonthlyAggregatedRecordWithTitleBean> records = repository.aggregateByMonthAndPersonForBudget(1l, format.parse("01.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
        Assert.assertEquals("person1", records.get(0).getTitle());

    }

    @Test
    @DatabaseSetup("aggregateByMonthForBudget.xml")
    @DatabaseTearDown(value = "aggregateByMonthForBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthForBudget() throws ParseException {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthForBudget(1l, format.parse("01.01.2015"));
        Assert.assertEquals(1, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
    }

}
