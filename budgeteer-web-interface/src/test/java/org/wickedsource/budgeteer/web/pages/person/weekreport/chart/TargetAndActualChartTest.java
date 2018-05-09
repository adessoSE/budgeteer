package org.wickedsource.budgeteer.web.pages.person.weekreport.chart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.statistics.MoneySeries;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;

import java.util.Random;

public class TargetAndActualChartTest extends AbstractWebTestTemplate {

    private Random random = new Random();

    @Test
    void testRender() {
        WicketTester tester = getTester();
        PersonWeeklyAggregationModel model = new PersonWeeklyAggregationModel(1L);
        tester.startComponentInPage(new TargetAndActualChart("chart", model, TargetAndActualChartConfiguration.Mode.WEEKLY));
    }

    public TargetAndActual createTargetAndActual(long personId, int numberOfWeeks) {
        TargetAndActual targetAndActual = new TargetAndActual();

        for (int i = 0; i < 5; i++) {
            MoneySeries series = new MoneySeries();
            series.setName("Budget " + i);
            for (int j = 0; j < numberOfWeeks; j++) {
                series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            }
            targetAndActual.getActualSeries().add(series);
        }

        MoneySeries series = new MoneySeries();
        series.setName("Target");
        for (int j = 0; j < numberOfWeeks; j++) {
            series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
        }
        targetAndActual.setTargetSeries(series);

        return targetAndActual;

    }

    @Override
    protected void setupTest() {

    }
}
