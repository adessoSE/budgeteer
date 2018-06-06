package org.wickedsource.budgeteer.web.pages.budgets.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.when;

public class BudgetHoursPageTest extends AbstractWebTestTemplate {

    @Autowired
    private BudgetService service;

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadBudgetBaseData(1L)).thenReturn(createBudget());
        tester.startPage(BudgetHoursPage.class, BudgetHoursPage.createParameters(1L));
        tester.assertRenderedPage(BudgetHoursPage.class);
    }

    private BudgetBaseData createBudget() {
        return new BudgetBaseData(1L, "Budget 1");
    }

    @Override
    protected void setupTest() {

    }
}
