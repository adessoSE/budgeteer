package org.wickedsource.budgeteer.web.pages.budgets.monthreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.pages.budgets.monthreport.multi.MultiBudgetMonthReportPage;

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
