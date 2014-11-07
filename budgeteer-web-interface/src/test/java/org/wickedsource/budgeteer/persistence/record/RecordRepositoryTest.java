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
import java.util.Date;
import java.util.List;

public class RecordRepositoryTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private RecordRepository repository;

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
    @DatabaseSetup("getPlannedBudget.xml")
    @DatabaseTearDown(value = "getPlannedBudget.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetPlannedBudget() throws Exception {
        double value = repository.getPlannedBudget(1l);
        Assert.assertEquals(170000d, value, 1d);
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
    public void testUpdateDailyRates() throws Exception {
        repository.updateDailyRates(1l, 1l, format.parse("01.01.2015"), format.parse("15.08.2015"), MoneyUtil.createMoneyFromCents(50000l));
        WorkRecordEntity record = repository.findOne(1l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
        record = repository.findOne(3l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000l), record.getDailyRate());
    }
}
