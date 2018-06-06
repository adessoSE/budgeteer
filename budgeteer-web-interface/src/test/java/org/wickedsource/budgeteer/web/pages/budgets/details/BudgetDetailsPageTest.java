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

    @Autowired
    private BudgetService service;

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadBudgetDetailData(1L)).thenReturn(createBudget());
        when(service.loadBudgetBaseData(1L)).thenReturn(new BudgetBaseData(1L, "Budget 1"));
        tester.startPage(BudgetDetailsPage.class, BudgetDetailsPage.createParameters(1L));
        tester.assertRenderedPage(BudgetDetailsPage.class);
    }

    private BudgetDetailData createBudget() {
        BudgetDetailData budgetDetailData = new BudgetDetailData();
        budgetDetailData.setId(1L);
        budgetDetailData.setName("Budget 1");
        budgetDetailData.setContractId(2L);
        budgetDetailData.setContractName("Contract");
        budgetDetailData.setTotal(Money.of(CurrencyUnit.EUR, 123));
        budgetDetailData.setSpent(Money.of(CurrencyUnit.EUR, 43));
        return budgetDetailData;
    }

    @Override
    protected void setupTest() {

    }
}
