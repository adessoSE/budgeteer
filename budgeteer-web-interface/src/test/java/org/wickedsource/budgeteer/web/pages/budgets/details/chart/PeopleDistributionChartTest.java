package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

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
