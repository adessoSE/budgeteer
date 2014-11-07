package org.wickedsource.budgeteer.service.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PersonServiceIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testLoadPersonWithRates() throws Exception {
        PersonWithRates person = service.loadPersonWithRates(2l);
        Assert.assertEquals("person2", person.getName());
        Assert.assertEquals(2l, person.getPersonId());
        Assert.assertEquals("person2", person.getImportKey());
        Assert.assertEquals(2, person.getRates().size());
        Assert.assertNotNull(person.getRates().get(0).getBudget());
    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testSavePersonWithRates() throws Exception {
        PersonWithRates person = new PersonWithRates();
        person.setPersonId(1l);
        person.setName("name");
        person.setImportKey("name");
        PersonRate rate1 = new PersonRate();
        rate1.setRate(MoneyUtil.createMoneyFromCents(12300l));
        rate1.setDateRange(new DateRange(format.parse("01.01.2014"), format.parse("15.08.2014")));
        rate1.setBudget(new BudgetBaseData(1l, "budget1"));
        person.getRates().add(rate1);
        PersonRate rate2 = new PersonRate();
        rate2.setRate(MoneyUtil.createMoneyFromCents(32100l));
        rate2.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("15.08.2015")));
        rate2.setBudget(new BudgetBaseData(1l, "budget1"));
        person.getRates().add(rate2);
        service.savePersonWithRates(person);

        PersonEntity personEntity = personRepository.findOne(1l);
        Assert.assertEquals(2, personEntity.getDailyRates().size());

        WorkRecordEntity record = recordRepository.findOne(1l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(32100l), record.getDailyRate());
        record = recordRepository.findOne(2l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(12300l), record.getDailyRate());
        record = recordRepository.findOne(3l);
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(32100l), record.getDailyRate());
    }

}
