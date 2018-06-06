package org.wickedsource.budgeteer.web.pages.budgets.details;

import org.apache.wicket.util.tester.WicketTester;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.when;

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
