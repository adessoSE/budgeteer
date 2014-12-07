package org.wickedsource.budgeteer.persistence.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PersonRepositoryTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindBaseDataByProjectId() throws Exception {
        List<PersonBaseDataBean> data = personRepository.findBaseDataByProjectId(1l);
        Assert.assertEquals(2, data.size());

        PersonBaseDataBean bean1 = data.get(0);
        Assert.assertEquals(Long.valueOf(1), bean1.getId());
        Assert.assertEquals(Long.valueOf(20000), bean1.getAverageDailyRateInCents());
        Assert.assertEquals(format.parse("15.08.2015"), bean1.getLastBookedDate());
        Assert.assertEquals("person1", bean1.getName());

        PersonBaseDataBean bean2 = data.get(1);
        Assert.assertEquals(Long.valueOf(2), bean2.getId());
        Assert.assertEquals(Long.valueOf(47500), bean2.getAverageDailyRateInCents());
        Assert.assertEquals(format.parse("15.08.2016"), bean2.getLastBookedDate());
        Assert.assertEquals("person2", bean2.getName());
    }

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindBaseDataByPersonId() throws Exception {
        PersonBaseDataBean bean = personRepository.findBaseDataByPersonId(1l);

        Assert.assertEquals(Long.valueOf(1), bean.getId());
        Assert.assertEquals(Long.valueOf(20000), bean.getAverageDailyRateInCents());
        Assert.assertEquals(format.parse("15.08.2015"), bean.getLastBookedDate());
        Assert.assertEquals("person1", bean.getName());
    }

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDetailDataByPersonId() throws Exception {
        PersonDetailDataBean bean = personRepository.findDetailDataByPersonId(1l);

        Assert.assertEquals(Long.valueOf(1), bean.getId());
        Assert.assertEquals("person1", bean.getName());
        Assert.assertEquals(Long.valueOf(20000), bean.getAverageDailyRateInCents());
        Assert.assertEquals(format.parse("01.01.2014"), bean.getFirstBookedDate());
        Assert.assertEquals(format.parse("15.08.2015"), bean.getLastBookedDate());
        Assert.assertEquals(Long.valueOf(60000), bean.getBudgetBurnedInCents());
        Assert.assertEquals(24d, bean.getHoursBooked(), 1d);

    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindOneFetchDailyRates() {
        PersonEntity person = personRepository.findOneFetchDailyRates(2l);
        Assert.assertEquals(2, person.getDailyRates().size());
        Assert.assertNotNull(person.getDailyRates().get(0).getBudget());
    }
}
