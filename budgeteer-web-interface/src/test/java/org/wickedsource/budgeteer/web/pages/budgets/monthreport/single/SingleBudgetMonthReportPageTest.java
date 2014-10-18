package org.wickedsource.budgeteer.web.pages.budgets.monthreport.single;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class SingleBudgetMonthReportPageTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        tester.startPage(SingleBudgetMonthReportPage.class, SingleBudgetMonthReportPage.createParameters(1l));
        tester.assertRenderedPage(SingleBudgetMonthReportPage.class);
    }
}
