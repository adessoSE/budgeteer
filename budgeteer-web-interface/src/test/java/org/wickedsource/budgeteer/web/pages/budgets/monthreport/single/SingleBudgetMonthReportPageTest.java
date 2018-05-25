package org.wickedsource.budgeteer.web.pages.budgets.monthreport.single;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class SingleBudgetMonthReportPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(SingleBudgetMonthReportPage.class, SingleBudgetMonthReportPage.createParameters(1L));
        tester.assertRenderedPage(SingleBudgetMonthReportPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
