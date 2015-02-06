package org.wickedsource.budgeteer.web.pages.budgets.overview.filter;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetTagsModel;

public class BudgetTagFilterPanelTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        BudgetTagFilterPanel panel = new BudgetTagFilterPanel("panel", new BudgetTagsModel(1l));
        tester.startComponentInPage(panel);
    }

}
