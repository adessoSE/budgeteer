package org.wickedsource.budgeteer.persistence.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class PersonRepositoryTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindBaseDataByProjectId() throws Exception {
        List<PersonBaseDataBean> data = personRepository.findBaseDataByProjectId(1L);
        Assertions.assertEquals(2, data.size());

        PersonBaseDataBean bean1 = data.get(0);
        Assertions.assertEquals(Long.valueOf(1), bean1.getId());
        Assertions.assertEquals(Long.valueOf(20000), bean1.getAverageDailyRateInCents());
        Assertions.assertEquals(format.parse("15.08.2015"), bean1.getLastBookedDate());
        Assertions.assertEquals("person1", bean1.getName());

        PersonBaseDataBean bean2 = data.get(1);
        Assertions.assertEquals(Long.valueOf(2), bean2.getId());
        Assertions.assertEquals(Long.valueOf(47500), bean2.getAverageDailyRateInCents());
        Assertions.assertEquals(format.parse("15.08.2016"), bean2.getLastBookedDate());
        Assertions.assertEquals("person2", bean2.getName());
    }

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindBaseDataByPersonId() throws Exception {
        PersonBaseDataBean bean = personRepository.findBaseDataByPersonId(1L);

        Assertions.assertEquals(Long.valueOf(1), bean.getId());
        Assertions.assertEquals(Long.valueOf(20000), bean.getAverageDailyRateInCents());
        Assertions.assertEquals(format.parse("15.08.2015"), bean.getLastBookedDate());
        Assertions.assertEquals("person1", bean.getName());
    }

    @Test
    @DatabaseSetup("findBaseData.xml")
    @DatabaseTearDown(value = "findBaseData.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindDetailDataByPersonId() throws Exception {
        PersonDetailDataBean bean = personRepository.findDetailDataByPersonId(1L);

        Assertions.assertEquals(Long.valueOf(1), bean.getId());
        Assertions.assertEquals("person1", bean.getName());
        Assertions.assertEquals(Long.valueOf(20000), bean.getAverageDailyRateInCents());
        Assertions.assertEquals(format.parse("01.01.2014"), bean.getFirstBookedDate());
        Assertions.assertEquals(format.parse("15.08.2015"), bean.getLastBookedDate());
        Assertions.assertEquals(Long.valueOf(60000), bean.getBudgetBurnedInCents());
        Assertions.assertEquals(24d, bean.getHoursBooked(), 1d);

    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindOneFetchDailyRates() {
        PersonEntity person = personRepository.findOneFetchDailyRates(2L);
        Assertions.assertEquals(2, person.getDailyRates().size());
        Assertions.assertNotNull(person.getDailyRates().get(0).getBudget());
    }
}
