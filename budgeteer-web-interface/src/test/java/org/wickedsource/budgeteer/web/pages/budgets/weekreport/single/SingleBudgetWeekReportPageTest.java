package org.wickedsource.budgeteer.web.pages.budgets.weekreport.single;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class SingleBudgetWeekReportPageTest extends AbstractWebTestTemplate{

    @Test
    void render(){
        WicketTester tester = getTester();
        tester.startPage(SingleBudgetWeekReportPage.class, SingleBudgetWeekReportPage.createParameters(1L));
        tester.assertRenderedPage(SingleBudgetWeekReportPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
