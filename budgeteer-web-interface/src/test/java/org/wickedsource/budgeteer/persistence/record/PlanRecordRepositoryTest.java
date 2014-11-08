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
    @DatabaseSetup("aggregateByWeek.xml")
    @DatabaseTearDown(value = "aggregateByWeek.xml", type = DatabaseOperation.DELETE_ALL)
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

}
