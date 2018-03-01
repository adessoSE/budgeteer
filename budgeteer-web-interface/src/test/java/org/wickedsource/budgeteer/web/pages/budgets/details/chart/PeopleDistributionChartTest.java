package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PeopleDistributionChartTest extends AbstractWebTestTemplate {

	@Test
	public void testRender() {
        WicketTester tester = getTester();
        PeopleDistributionChartModel model = new PeopleDistributionChartModel(1l);
        tester.startComponentInPage(new PeopleDistributionChart("chart", model));
	}

}
