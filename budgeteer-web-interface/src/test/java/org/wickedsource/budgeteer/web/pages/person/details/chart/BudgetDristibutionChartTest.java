package org.wickedsource.budgeteer.web.pages.person.details.chart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BudgetDristibutionChartTest extends AbstractWebTestTemplate{

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        BudgetDistributionChartModel model = new BudgetDistributionChartModel(1l);
        tester.startComponentInPage(new BudgetDistributionChart("chart", model));
    }
}
