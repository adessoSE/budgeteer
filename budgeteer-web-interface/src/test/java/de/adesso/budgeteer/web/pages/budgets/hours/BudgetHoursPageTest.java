package de.adesso.budgeteer.web.pages.budgets.hours;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BudgetHoursPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(BudgetHoursPage.class, BudgetHoursPage.createParameters(1L));
        tester.assertRenderedPage(BudgetHoursPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
