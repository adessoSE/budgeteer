package de.adesso.budgeteer.web.pages.contract.details.contractDetailChart;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChart;
import de.adesso.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChartModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class ContractDetailChartTest extends AbstractWebTestTemplate {


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
