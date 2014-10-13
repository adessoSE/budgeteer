package org.wickedsource.budgeteer.web.usecase.budgets.overview.filter;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BudgetTagFilterPanelTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        BudgetTagFilterPanel panel = new BudgetTagFilterPanel("panel", new BudgetTagsModel(1l), Model.of(new BudgetTagFilter()));
        tester.startComponentInPage(panel);
    }

}
