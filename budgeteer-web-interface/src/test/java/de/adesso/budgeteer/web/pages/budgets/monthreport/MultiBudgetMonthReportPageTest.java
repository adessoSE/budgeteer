package de.adesso.budgeteer.web.pages.budgets.monthreport;

import de.adesso.budgeteer.service.budget.BudgetTagFilter;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.pages.budgets.monthreport.multi.MultiBudgetMonthReportPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MultiBudgetMonthReportPageTest extends AbstractWebTestTemplate {

    @Test
    void testSingleBudget() {
        WicketTester tester = getTester();
        BudgetTagFilter filter = new BudgetTagFilter(Collections.EMPTY_LIST, 1L);
        BudgeteerSession.get().setBudgetFilter(filter);
        tester.startPage(MultiBudgetMonthReportPage.class);
        tester.assertRenderedPage(MultiBudgetMonthReportPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
