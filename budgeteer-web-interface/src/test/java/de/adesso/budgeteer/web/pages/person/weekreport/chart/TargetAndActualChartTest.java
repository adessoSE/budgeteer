package de.adesso.budgeteer.web.pages.person.weekreport.chart;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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