package de.adesso.budgeteer.web.pages.budgets.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

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
