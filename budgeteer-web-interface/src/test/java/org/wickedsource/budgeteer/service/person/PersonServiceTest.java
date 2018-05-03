package org.wickedsource.budgeteer.service.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
        Assertions.assertEquals(1, data.size());
        Assertions.assertEquals(Long.valueOf(1), data.get(0).getId());
        Assertions.assertEquals("person1", data.get(0).getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(10000l), data.get(0).getAverageDailyRate());
        Assertions.assertEquals(fixedDate, data.get(0).getLastBooked());
    }


    @Test
    public void testLoadPersonDetailData() throws Exception {
        when(personRepository.findDetailDataByPersonId(1l)).thenReturn(createPersonDetailDataBean());
        PersonDetailData data = personService.loadPersonDetailData(1l);
        Assertions.assertEquals("person1", data.getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(123456l), data.getAverageDailyRate());
        Assertions.assertEquals(fixedDate, data.getLastBookedDate());
        Assertions.assertEquals(fixedDate, data.getFirstBookedDate());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(654321l), data.getBudgetBurned());
        Assertions.assertEquals(5.0d, data.getHoursBooked(), 0.1d);
    }


    @Test
    public void testLoadPersonBaseData() throws Exception {
        when(personRepository.findBaseDataByPersonId(1l)).thenReturn(createPersonBaseDataBean());
        PersonBaseData bean = personService.loadPersonBaseData(1l);
        Assertions.assertEquals(Long.valueOf(1), bean.getId());
        Assertions.assertEquals("person1", bean.getName());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(10000l), bean.getAverageDailyRate());
        Assertions.assertEquals(fixedDate, bean.getLastBooked());
    }

    private PersonBaseDataBean createPersonBaseDataBean() {
        return new PersonBaseDataBean(1l, "person1", 10000l, fixedDate);
    }

    private PersonDetailDataBean createPersonDetailDataBean() {
        return new PersonDetailDataBean(1l, "person1", 123456l, fixedDate, fixedDate, 5.0d, 654321l);
    }
}
