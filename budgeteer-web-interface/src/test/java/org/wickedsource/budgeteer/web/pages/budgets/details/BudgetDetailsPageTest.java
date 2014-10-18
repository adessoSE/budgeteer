package org.wickedsource.budgeteer.web.pages.budgets.details;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.when;

public class BudgetDetailsPageTest extends AbstractWebTestTemplate {

    @Autowired
    private BudgetService service;

    @Test
    public void render() {
        WicketTester tester = getTester();
        when(service.loadBudgetBaseData(1l)).thenReturn(createBudget());
        tester.startPage(BudgetDetailsPage.class, BudgetDetailsPage.createParameters(1l));
        tester.assertRenderedPage(BudgetDetailsPage.class);
    }

    private BudgetBaseData createBudget() {
        return new BudgetBaseData(1l, "Budget 1");
    }
}
