package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.table.FixedDailyRatesTable;


public class FixedDailyRatesPageTest extends AbstractWebTestTemplate {

    @Autowired
    private FixedDailyRateService fixedDailyRateService;

    @Test
    void render() {
        WicketTester tester = getTester();
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);
        tester.startPage(FixedDailyRatesPage.class, parameters);
        tester.assertRenderedPage(FixedDailyRatesPage.class);
    }

    @Override
    protected void setupTest() {

    }
}