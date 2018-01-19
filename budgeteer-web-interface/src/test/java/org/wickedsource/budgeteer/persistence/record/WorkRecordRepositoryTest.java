package org.wickedsource.budgeteer.persistence.record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

public class WorkRecordRepositoryTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private WorkRecordRepository repository;

    @Test
    @DatabaseSetup("getSpentBudget.xml")
    @DatabaseTearDown(value = "getSpentBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetSpentBudget() throws Exception {
        double value = repository.getSpentBudget(1l);
        Assert.assertEquals(170000d, value, 1d);
    }

    @Test
    @DatabaseSetup("getAverageDailyRate.xml")
    @DatabaseTearDown(value = "getAverageDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAverageDailyRate() throws Exception {
        double value = repository.getAverageDailyRate(1l);
        Assert.assertEquals(56666d, value, 1d);
    }

    @Test
    @DatabaseSetup("getAverageDailyRateZeroMinutes.xml")
    @DatabaseTearDown(value = "getAverageDailyRate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAverageDailyRateZeroMinutes() throws Exception {
        double value = repository.getAverageDailyRate(1l);
        Assert.assertEquals(0, value, 0);
    }

    @Test
    @DatabaseSetup("getLastWorkRecordDate.xml")
    @DatabaseTearDown(value = "getLastWorkRecordDate.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetLatestWorkRecordDate() throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = repository.getLatestWordRecordDate(1l);
        Assert.assertEquals(format.parse("2015-08-15"), date);
    }

    @Test
    @DatabaseSetup("getMissingDailyRates.xml")
    @DatabaseTearDown(value = "getMissingDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetMissingDailyRatesForProject() throws Exception {
        List<MissingDailyRateBean> missingRates = repository.getMissingDailyRatesForProject(1l);
        Assert.assertEquals(2, missingRates.size());
        Assert.assertEquals(1l, missingRates.get(0).getPersonId());
        Assert.assertEquals("person1", missingRates.get(0).getPersonName());
        Assert.assertEquals(format.parse("01.01.2015"), missingRates.get(0).getStartDate());
        Assert.assertEquals(format.parse("15.08.2015"), missingRates.get(0).getEndDate());
        Assert.assertEquals(2l, missingRates.get(1).getPersonId());
        Assert.assertEquals("person2", missingRates.get(1).getPersonName());
        Assert.assertEquals(format.parse("01.01.2015"), missingRates.get(1).getStartDate());
        Assert.assertEquals(format.parse("15.08.2015"), missingRates.get(1).getEndDate());
    }

    @Test
    @DatabaseSetup("getMissingDailyRates.xml")
    @DatabaseTearDown(value = "getMissingDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetMissingDailyRatesForPerson() throws Exception {
        List<MissingDailyRateForBudgetBean> missingRates = repository.getMissingDailyRatesForPerson(1l);
        Assert.assertEquals(2, missingRates.size());

        Assert.assertEquals(1l, missingRates.get(0).getPersonId());
        Assert.assertEquals("person1", missingRates.get(0).getPersonName());
        Assert.assertEquals(format.parse("01.01.2015"), missingRates.get(0).getStartDate());
        Assert.assertEquals(format.parse("01.01.2015"), missingRates.get(0).getEndDate());
        Assert.assertEquals("Budget 1", missingRates.get(0).getBudgetName());
    }

    @Test
    @DatabaseSetup("updateDailyRates.xml")
    @DatabaseTearDown(value = "updateDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdateWorkRecordDailyRates() throws Exception {
        repository.updateDailyRates(1l, 1l, format.parse("01.01.2015"), format.parse("15.08.2015"), MoneyUtil.createMoneyFromCents(50000l));
        WorkRecordEntity record = repository.findOne(1l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
        record = repository.findOne(3l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
    }

    @Test
    @DatabaseSetup("updateDailyRates.xml")
    @DatabaseTearDown(value = "updateDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdateWorkRecordDailyRatesWithEditedRecord() throws Exception {
        repository.updateDailyRates(1l, 1l, format.parse("01.01.2015"), format.parse("15.08.2015"), MoneyUtil.createMoneyFromCents(50000l));
        WorkRecordEntity record = repository.findOne(1l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
        record = repository.findOne(6l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), record.getDailyRate());
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
    @DatabaseSetup("aggregateByWeekAndBudgetForPerson.xml")
    @DatabaseTearDown(value = "aggregateByWeekAndBudgetForPerson.xml", type = DatabaseOperation.DELETE_ALL)
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
    @DatabaseSetup("getAverageDailyRatePerDay.xml")
    @DatabaseTearDown(value = "getAverageDailyRatePerDay.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAverageDailyRatePerDay() throws ParseException {
        List<DailyAverageRateBean> records = repository.getAverageDailyRatesPerDay(1l, format.parse("01.01.2015"));
        Assert.assertEquals(2, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(1, records.get(0).getDay());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(15000l), records.get(0).getRate());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(2, records.get(1).getDay());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(25000l), records.get(1).getRate());
    }

    @Test
    @DatabaseSetup("getBudgetShareForPerson.xml")
    @DatabaseTearDown(value = "getBudgetShareForPerson.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetBudgetShareForPerson() throws ParseException {
        List<ShareBean> records = repository.getBudgetShareForPerson(1l);
        Assert.assertEquals(4, records.size());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), records.get(0).getValue());
        Assert.assertEquals("Budget 1", records.get(0).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(20000l), records.get(1).getValue());
        Assert.assertEquals("Budget 2", records.get(1).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(30000l), records.get(2).getValue());
        Assert.assertEquals("Budget 3", records.get(2).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(40000l), records.get(3).getValue());
        Assert.assertEquals("Budget 4", records.get(3).getName());
    }

    @Test
    @DatabaseSetup("getPersonShareForBudget.xml")
    @DatabaseTearDown(value = "getPersonShareForBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetPersonShareForBudget() throws ParseException {
        List<ShareBean> records = repository.getPersonShareForBudget(1l);
        Assert.assertEquals(4, records.size());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), records.get(0).getValue());
        Assert.assertEquals("person1", records.get(0).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(20000l), records.get(1).getValue());
        Assert.assertEquals("person2", records.get(1).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(30000l), records.get(2).getValue());
        Assert.assertEquals("person3", records.get(2).getName());

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(40000l), records.get(3).getValue());
        Assert.assertEquals("person4", records.get(3).getName());
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

    @Test
    @DatabaseSetup("aggregateByWeekForBudget.xml")
    @DatabaseTearDown(value = "aggregateByWeekForBudget.xml", type = DatabaseOperation.DELETE_ALL)
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
    @DatabaseSetup("aggregateByMonthForBudgets.xml")
    @DatabaseTearDown(value = "aggregateByMonthForBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthForBudgets() throws ParseException {
        List<MonthlyAggregatedRecordBean> records = repository.aggregateByMonthForBudgets(1l, Arrays.asList("tag1"), format.parse("01.01.2015"));
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
    @DatabaseSetup("aggregateByMonthAndPersonForBudgets.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndPersonForBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAggregateByMonthAndPersonForBudgets() throws ParseException {
        List<MonthlyAggregatedRecordWithTitleBean> records = repository.aggregateByMonthAndPersonForBudgets(1l, Arrays.asList("tag1"), format.parse("01.01.2015"));
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(2015, records.get(0).getYear());
        Assert.assertEquals(0, records.get(0).getMonth());
        Assert.assertEquals(8d, records.get(0).getHours(), 0.1d);
        Assert.assertEquals(10000l, records.get(0).getValueInCents());
        Assert.assertEquals("person1", records.get(0).getTitle());

        Assert.assertEquals(2015, records.get(1).getYear());
        Assert.assertEquals(0, records.get(1).getMonth());
        Assert.assertEquals(8d, records.get(1).getHours(), 0.1d);
        Assert.assertEquals(20000l, records.get(1).getValueInCents());
        Assert.assertEquals("person2", records.get(1).getTitle());

        Assert.assertEquals(2015, records.get(2).getYear());
        Assert.assertEquals(7, records.get(2).getMonth());
        Assert.assertEquals(16d, records.get(2).getHours(), 0.1d);
        Assert.assertEquals(0l, records.get(2).getValueInCents());
        Assert.assertEquals("person2", records.get(2).getTitle());
    }

    @Test
    @DatabaseSetup("aggregateByMonthAndPersonForBudgets.xml")
    @DatabaseTearDown(value = "aggregateByMonthAndPersonForBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDeleteByProjectId(){
        repository.deleteByProjectId(1l);
    }

    @Test
    @DatabaseSetup("countByProjectId.xml")
    @DatabaseTearDown(value = "countByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
    public void testCountByProjectId(){
        Assert.assertEquals(6, (long) repository.countByProjectId(1l));
        Assert.assertEquals(0, (long) repository.countByProjectId(2l));
    }

    @Test
    @DatabaseSetup("findManuallyEditedEntries.xml")
    @DatabaseTearDown(value = "findManuallyEditedEntries.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindManuallyEditedEntries() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<WorkRecordEntity> records = repository.findManuallyEditedEntries(2l, formatter.parse("2014-01-01"), new Date() );
        Assert.assertEquals(0, records.size());

        records = repository.findManuallyEditedEntries(1l, formatter.parse("2014-01-01"), formatter.parse("2016-08-15") );
        Assert.assertEquals(1, records.size());

        records = repository.findManuallyEditedEntries(1l, formatter.parse("2014-01-01"), formatter.parse("2016-08-14") );
        Assert.assertEquals(0, records.size());

        records = repository.findManuallyEditedEntries(1l, formatter.parse("2012-01-01"), formatter.parse("2016-08-14") );
        Assert.assertEquals(1, records.size());

        records = repository.findManuallyEditedEntries(1l, formatter.parse("2012-01-01"), formatter.parse("2016-08-16") );
        Assert.assertEquals(2, records.size());
    }

    @Test
    @DatabaseSetup("findDuplicateEntries.xml")
    @DatabaseTearDown(value = "findDuplicateEntries.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDuplicateEntries() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        BudgetEntity budget = new BudgetEntity(); budget.setId(1l);
        PersonEntity person = new PersonEntity(); person.setId(1l);


        List<WorkRecordEntity> records = repository.findDuplicateEntries(budget, person, formatter.parse("2014-01-01"), 480);
        Assert.assertEquals(1, records.size());

        records = repository.findDuplicateEntries(budget, person, formatter.parse("2014-01-01"), 481);
        Assert.assertEquals(0, records.size());

        person.setId(3l);
        budget.setId(4l);
        records = repository.findDuplicateEntries(budget, person, formatter.parse("2016-08-15"), 960);
        Assert.assertEquals(2, records.size());

    }
}
