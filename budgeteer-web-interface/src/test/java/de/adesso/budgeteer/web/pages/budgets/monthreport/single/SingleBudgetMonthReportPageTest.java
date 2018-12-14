package de.adesso.budgeteer.web.pages.budgets.monthreport.single;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

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
