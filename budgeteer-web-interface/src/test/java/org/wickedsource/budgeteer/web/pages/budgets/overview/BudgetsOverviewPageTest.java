package org.wickedsource.budgeteer.web.pages.budgets.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class BudgetsOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        tester.assertRenderedPage(BudgetsOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }

    @Test
    public void netGrossLinkWithManDays(){
        BudgeteerSession.get().setTaxEnabled(true);
        BudgeteerSession.get().setSelectedBudgetUnit(13.0);
        getTester().clickLink("netGrossLink");
        Assert.assertTrue(BudgeteerSession.get().isTaxEnabled());
    }

    @Test
    public void netGrossLink(){
        BudgeteerSession.get().setTaxEnabled(true);
        BudgeteerSession.get().setSelectedBudgetUnit(1.0);
        getTester().clickLink("netGrossLink");
        Assert.assertFalse(BudgeteerSession.get().isTaxEnabled());
    }
}
