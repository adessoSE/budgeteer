package de.adesso.budgeteer.web.pages.budgets.details.chart;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class PeopleDistributionChartTest extends AbstractWebTestTemplate {

	@Test
    void testRender() {
        WicketTester tester = getTester();
        PeopleDistributionChartModel model = new PeopleDistributionChartModel(1L);
        tester.startComponentInPage(new PeopleDistributionChart("chart", model));
	}

    @Override
    protected void setupTest() {

    }
}
