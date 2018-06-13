package org.wickedsource.budgeteer.web.pages.budgets.details;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

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
