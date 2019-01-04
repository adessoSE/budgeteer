package de.adesso.budgeteer.web.pages.budgets.weekreport.single;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
