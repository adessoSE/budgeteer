package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChart;
import org.wickedsource.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChartModel;

public class ContractDetailChartTest extends AbstractWebTestTemplate {


	@Test
	public void testRender() {
        WicketTester tester = getTester();
        PeopleDistributionChartModel model = new PeopleDistributionChartModel(1l);
        tester.startComponentInPage(new PeopleDistributionChart("chart", model));
	}

}
