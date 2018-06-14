package org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BurnedBudgetChartTest extends AbstractWebTestTemplate  {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        BurnedBudgetChartModel model = new BurnedBudgetChartModel(1L, 5);
        tester.startComponentInPage(new BurnedBudgetChart("chart", model));
    }

    @Override
    protected void setupTest() {

    }
}
