package org.wickedsource.budgeteer.persistence.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class DailyRateRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private DailyRateRepository rateRepository;

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetDistinctRates() {
        List<Money> rates = rateRepository.getDistinctRatesInCents(1L);
        Assertions.assertEquals(2, rates.size());
        Assertions.assertTrue(rates.contains(MoneyUtil.createMoneyFromCents(50000L)));
        Assertions.assertTrue(rates.contains(MoneyUtil.createMoneyFromCents(60000L)));
    }


    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByBudgetAndPersonWithOverlappingDateRange() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        List<DailyRateEntity> rates = rateRepository.findByBudgetAndPersonWithOverlappingDateRange(2L, 1L, formatter.parse("20.02.2015"), formatter.parse("25.02.2015"));

        Assertions.assertEquals(4, rates.size());
        List<Long> idsThatShouldBePresent = new LinkedList<Long>();
        idsThatShouldBePresent.addAll(Arrays.asList(4L, 5L, 6L, 7L));
        for(int i=idsThatShouldBePresent.size()-1; i >= 0; i--){
            DailyRateEntity r = rates.get(i);
            idsThatShouldBePresent.remove(r.getId());
        }
        Assertions.assertTrue(idsThatShouldBePresent.isEmpty());
    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByBudgetAndPersonEndingInOrAfterDateRange() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        List<DailyRateEntity> rates = rateRepository.findByBudgetAndPersonEndingInOrAfterDateRange(2L, 1L, formatter.parse("21.02.2015"));

        Assertions.assertEquals(4, rates.size());
        List<Long> idsThatShouldBePresent = new LinkedList<Long>();
        idsThatShouldBePresent.addAll(Arrays.asList(5L, 6L, 7L, 8L));
        for(int i=idsThatShouldBePresent.size()-1; i >= 0; i--){
            DailyRateEntity r = rates.get(i);
            idsThatShouldBePresent.remove(r.getId());
        }
        Assertions.assertTrue(idsThatShouldBePresent.isEmpty());
    }

}
