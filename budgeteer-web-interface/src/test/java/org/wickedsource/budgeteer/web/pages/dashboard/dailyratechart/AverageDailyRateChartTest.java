package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class AverageDailyRateChartTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        AverageDailyRateChartModel model = new AverageDailyRateChartModel(1L, 5);
        tester.startComponentInPage(new AverageDailyRateChart("chart", model));
    }

    @Override
    protected void setupTest() {

    }
}