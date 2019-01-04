package de.adesso.budgeteer.web.pages.budgets.overview;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        tester.clickLink("netGrossLink:link");
        Assertions.assertTrue(BudgeteerSession.get().isTaxEnabled());
    }

    @Test
    public void netGrossLink(){
        WicketTester tester = getTester();
        tester.startPage(BudgetsOverviewPage.class);
        BudgeteerSession.get().setTaxEnabled(true);
        BudgeteerSession.get().setSelectedBudgetUnit(1.0);
        tester.clickLink("netGrossLink:link");
        Assertions.assertFalse(BudgeteerSession.get().isTaxEnabled());
    }
}