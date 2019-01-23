package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.table;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateModel;
import org.wickedsource.budgeteer.service.DateRange;

import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class FixedDailyRatesTableTest extends AbstractWebTestTemplate {

    @Autowired
    private FixedDailyRateService service;
    private Random random = new Random();

    @Override
    protected void setupTest() {

    }

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadFixedDailyRate(anyLong())).thenReturn(createTestRate());
        when(service.getFixedDailyRates(anyLong())).thenReturn(createTestData());

        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);
        FixedDailyRateModel model = new FixedDailyRateModel(1L, service);
        FixedDailyRatesTable table = new FixedDailyRatesTable("table", model, parameters);

        tester.startComponentInPage(table);
    }

    private FixedDailyRate createTestRate() {
        Calendar calendar = new GregorianCalendar(2016, 1, 3);
        Date date = calendar.getTime();

        return new FixedDailyRate(1, 1, MoneyUtil.createMoneyFromCents(1000), date, date,
                "test rate", "test", new BigDecimal(10), 1);
    }

    private List<FixedDailyRate> createTestData() {
        List<FixedDailyRate> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            FixedDailyRate data = new FixedDailyRate();
            data.setStartDate(new Date());
            data.setId(i);
            data.setDays(1);
            data.setMoneyAmount(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            data.setTaxRate(new BigDecimal(10));
            data.setDescription("rate " + i);
            data.setName("rate " + i);
            data.setDateRange(new DateRange(new Date(), new Date()));
            data.setBudgetId(1L);
            data.setEndDate(new Date());

            list.add(data);
        }
        return list;
    }
}
