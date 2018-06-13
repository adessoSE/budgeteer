package org.wickedsource.budgeteer.web.pages.person.weekreport.chart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;

public class TargetAndActualChartTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        PersonWeeklyAggregationModel model = new PersonWeeklyAggregationModel(1L);
        tester.startComponentInPage(new TargetAndActualChart("chart", model, TargetAndActualChartConfiguration.Mode.WEEKLY));
    }

    @Override
    protected void setupTest() {

    }
}
