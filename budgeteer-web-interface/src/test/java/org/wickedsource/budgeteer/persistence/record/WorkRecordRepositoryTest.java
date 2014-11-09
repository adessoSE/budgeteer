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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        MissingDailyRateBean missingRates = repository.getMissingDailyRatesForPerson(1l);
        Assert.assertEquals(1l, missingRates.getPersonId());
        Assert.assertEquals("person1", missingRates.getPersonName());
        Assert.assertEquals(format.parse("01.01.2015"), missingRates.getStartDate());
        Assert.assertEquals(format.parse("15.08.2015"), missingRates.getEndDate());
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
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
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
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
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
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
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
    @DatabaseSetup("aggregate.xml")
    @DatabaseTearDown(value = "aggregate.xml", type = DatabaseOperation.DELETE_ALL)
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

}
