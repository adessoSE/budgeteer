package org.wickedsource.budgeteer.persistence.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DailyRateRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private DailyRateRepository rateRepository;

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetDistinctRates() {
        List<Money> rates = rateRepository.getDistinctRatesInCents(1l);
        Assert.assertEquals(2, rates.size());
        Assert.assertTrue(rates.contains(MoneyUtil.createMoneyFromCents(50000l)));
        Assert.assertTrue(rates.contains(MoneyUtil.createMoneyFromCents(60000l)));
    }


    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByBudgetAndPersonWithOverlappingDateRange() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        List<DailyRateEntity> rates = rateRepository.findByBudgetAndPersonWithOverlappingDateRange(2l, 1l, formatter.parse("20.02.2015"), formatter.parse("25.02.2015"));

        Assert.assertEquals(4, rates.size());
        List<Long> idsThatShouldBePresent = new LinkedList<Long>();
        idsThatShouldBePresent.addAll(Arrays.asList(new Long[]{4l,5l,6l,7l}));
        for(int i=idsThatShouldBePresent.size()-1; i >= 0; i--){
            DailyRateEntity r = rates.get(i);
            idsThatShouldBePresent.remove(r.getId());
        }
        Assert.assertTrue(idsThatShouldBePresent.isEmpty());
    }

    @Test
    @DatabaseSetup("personWithRates.xml")
    @DatabaseTearDown(value = "personWithRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByBudgetAndPersonEndingInOrAfterDateRange() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        List<DailyRateEntity> rates = rateRepository.findByBudgetAndPersonEndingInOrAfterDateRange(2l, 1l, formatter.parse("21.02.2015"));

        Assert.assertEquals(4, rates.size());
        List<Long> idsThatShouldBePresent = new LinkedList<Long>();
        idsThatShouldBePresent.addAll(Arrays.asList(new Long[]{5l,6l,7l, 8l}));
        for(int i=idsThatShouldBePresent.size()-1; i >= 0; i--){
            DailyRateEntity r = rates.get(i);
            idsThatShouldBePresent.remove(r.getId());
        }
        Assert.assertTrue(idsThatShouldBePresent.isEmpty());
    }

}
