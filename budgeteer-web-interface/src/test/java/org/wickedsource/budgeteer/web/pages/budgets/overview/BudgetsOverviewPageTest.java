package org.wickedsource.budgeteer.web.pages.budgets.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class BudgetsOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        tester.assertRenderedPage(BudgetsOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }

    @Test
    public void netGrossLinkWithManDays(){
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        BudgeteerSession.get().setTaxEnabled(true);
        BudgeteerSession.get().setSelectedBudgetUnit(13.0);
        tester.clickLink("netGrossLink");
        Assertions.assertTrue(BudgeteerSession.get().isTaxEnabled());
    }

    @Test
    public void netGrossLink(){
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        BudgeteerSession.get().setTaxEnabled(true);
        BudgeteerSession.get().setSelectedBudgetUnit(1.0);
        tester.clickLink("netGrossLink");
        Assertions.assertFalse(BudgeteerSession.get().isTaxEnabled());
    }
}
