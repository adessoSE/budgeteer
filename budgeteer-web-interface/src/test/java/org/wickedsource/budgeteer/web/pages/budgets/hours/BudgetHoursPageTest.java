package org.wickedsource.budgeteer.web.pages.budgets.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.when;

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
