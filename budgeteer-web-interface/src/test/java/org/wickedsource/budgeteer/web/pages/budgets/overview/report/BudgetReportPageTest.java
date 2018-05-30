package org.wickedsource.budgeteer.web.pages.budgets.overview.report;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;

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
