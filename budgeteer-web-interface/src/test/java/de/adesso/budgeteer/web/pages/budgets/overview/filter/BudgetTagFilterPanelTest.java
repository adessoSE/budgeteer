package de.adesso.budgeteer.web.pages.budgets.overview.filter;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.budgets.BudgetTagsModel;

public class BudgetTagFilterPanelTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        BudgetTagFilterPanel panel = new BudgetTagFilterPanel("panel", new BudgetTagsModel(1L));
        tester.startComponentInPage(panel);
    }

    @Override
    protected void setupTest() {

    }
}
