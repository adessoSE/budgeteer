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

}
