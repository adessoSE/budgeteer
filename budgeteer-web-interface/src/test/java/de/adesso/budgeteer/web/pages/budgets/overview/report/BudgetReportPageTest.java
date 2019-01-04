package de.adesso.budgeteer.web.pages.budgets.overview.report;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BudgetReportPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        tester.assertRenderedPage(BudgetsOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
