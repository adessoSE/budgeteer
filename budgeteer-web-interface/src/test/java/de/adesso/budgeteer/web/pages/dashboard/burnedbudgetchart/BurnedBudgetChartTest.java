package de.adesso.budgeteer.web.pages.dashboard.burnedbudgetchart;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
