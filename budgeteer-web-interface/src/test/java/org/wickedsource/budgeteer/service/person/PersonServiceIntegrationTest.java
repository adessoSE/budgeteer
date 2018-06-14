package org.wickedsource.budgeteer.service.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class PersonServiceIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testLoadPersonWithRates() throws Exception {
        PersonWithRates person = service.loadPersonWithRates(2L);
        Assertions.assertEquals("person2", person.getName());
        Assertions.assertEquals(2L, person.getPersonId());
        Assertions.assertEquals("person2", person.getImportKey());
        Assertions.assertEquals(2, person.getRates().size());
        Assertions.assertNotNull(person.getRates().get(0).getBudget());
    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testSavePersonWithRates() throws Exception {
        PersonWithRates person = new PersonWithRates();
        person.setPersonId(1L);
        person.setName("name");
        person.setImportKey("name");
        PersonRate rate1 = new PersonRate();
        rate1.setRate(MoneyUtil.createMoneyFromCents(12300L));
        rate1.setDateRange(new DateRange(format.parse("01.01.2014"), format.parse("15.08.2014")));
        rate1.setBudget(new BudgetBaseData(1L, "budget1"));
        person.getRates().add(rate1);
        PersonRate rate2 = new PersonRate();
        rate2.setRate(MoneyUtil.createMoneyFromCents(32100L));
        rate2.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("15.08.2015")));
        rate2.setBudget(new BudgetBaseData(1L, "budget1"));
        person.getRates().add(rate2);
        service.savePersonWithRates(person);

        PersonEntity personEntity = personRepository.findOne(1L);
        Assertions.assertEquals(2, personEntity.getDailyRates().size());

        WorkRecordEntity record = workRecordRepository.findOne(1L);
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(32100L), record.getDailyRate());
        record = workRecordRepository.findOne(2L);
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(12300L), record.getDailyRate());
        record = workRecordRepository.findOne(3L);
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(32100L), record.getDailyRate());
    }

}
