package org.wickedsource.budgeteer.service.person;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.person.PersonBaseDataBean;
import org.wickedsource.budgeteer.persistence.person.PersonDetailDataBean;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class PersonServiceTest extends ServiceTestTemplate {

    private Date fixedDate = new Date();

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testLoadPeopleBaseData() throws Exception {
        when(personRepository.findBaseDataByProjectId(1l)).thenReturn(Arrays.asList(createPersonBaseDataBean()));
        List<PersonBaseData> data = personService.loadPeopleBaseData(1l);
        Assert.assertEquals(1, data.size());
        Assert.assertEquals(1l, data.get(0).getId());
        Assert.assertEquals("person1", data.get(0).getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), data.get(0).getAverageDailyRate());
        Assert.assertEquals(fixedDate, data.get(0).getLastBooked());
    }


    @Test
    public void testLoadPersonDetailData() throws Exception {
        when(personRepository.findDetailDataByPersonId(1l)).thenReturn(createPersonDetailDataBean());
        PersonDetailData data = personService.loadPersonDetailData(1l);
        Assert.assertEquals("person1", data.getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(123456l), data.getAverageDailyRate());
        Assert.assertEquals(fixedDate, data.getLastBookedDate());
        Assert.assertEquals(fixedDate, data.getFirstBookedDate());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(654321l), data.getBudgetBurned());
        Assert.assertEquals(5.0d, data.getHoursBooked(), 0.1d);
    }


    @Test
    public void testLoadPersonBaseData() throws Exception {
        when(personRepository.findBaseDataByPersonId(1l)).thenReturn(createPersonBaseDataBean());
        PersonBaseData bean = personService.loadPersonBaseData(1l);
        Assert.assertEquals(1l, bean.getId());
        Assert.assertEquals("person1", bean.getName());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(10000l), bean.getAverageDailyRate());
        Assert.assertEquals(fixedDate, bean.getLastBooked());
    }

    private PersonBaseDataBean createPersonBaseDataBean() {
        return new PersonBaseDataBean(1l, "person1", 10000l, fixedDate);
    }

    private PersonDetailDataBean createPersonDetailDataBean() {
        return new PersonDetailDataBean(1l, "person1", 123456l, fixedDate, fixedDate, 5.0d, 654321l);
    }
}
