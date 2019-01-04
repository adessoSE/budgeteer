package de.adesso.budgeteer.web.pages.person.details.chart;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BudgetDristibutionChartTest extends AbstractWebTestTemplate{

    @Test
    void testRender() {
        WicketTester tester = getTester();
        BudgetDistributionChartModel model = new BudgetDistributionChartModel(1L);
        tester.startComponentInPage(new BudgetDistributionChart("chart", model));
    }

    @Override
    protected void setupTest() {

    }
}
