package de.adesso.budgeteer.web.pages.budgets.details;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BudgetDetailsPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(BudgetDetailsPage.class, BudgetDetailsPage.createParameters(1L));
        tester.assertRenderedPage(BudgetDetailsPage.class);
    }
    @Override
    protected void setupTest() {

    }
}
