package org.wickedsource.budgeteer.web.usecase.people.details.component.highlightspanel.budgetdistributionchart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.wickedcharts.BudgeteerChartTheme;

public class BudgetDristibutionChartTest extends AbstractWebTestTemplate{

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        BudgetDistributionChartModel model = new BudgetDistributionChartModel(1l);
        tester.startComponentInPage(new BudgetDistributionChart("chart", model, new BudgeteerChartTheme()));
    }
}
