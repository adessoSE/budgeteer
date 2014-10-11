package org.wickedsource.budgeteer.web.usecase.people.weekreport.component.targetactualchart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.statistics.BudgeteerSeries;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.wickedcharts.BudgeteerChartTheme;

import java.util.Random;

public class TargetAndActualChartTest extends AbstractWebTestTemplate{

//    @Autowired
//    private StatisticsService service;

    private Random random = new Random();

    @Test
    public void testRender() {
        WicketTester tester = getTester();
//        when(service.getWeekStatsForPerson(1l, 12)).thenReturn(createTargetAndActual(1l, 12));
        WeeklyTargetAndActualModel model = new WeeklyTargetAndActualModel(1l);
        tester.startComponentInPage(new TargetAndActualChart("chart", model, new BudgeteerChartTheme()));
    }

    public TargetAndActual createTargetAndActual(long personId, int numberOfWeeks) {
        TargetAndActual targetAndActual = new TargetAndActual();

        for (int i = 0; i < 5; i++) {
            BudgeteerSeries series = new BudgeteerSeries();
            series.setName("Budget " + i);
            for (int j = 0; j < numberOfWeeks; j++) {
                series.getValues().add(random.nextDouble());
            }
            targetAndActual.getActualSeries().add(series);
        }

        BudgeteerSeries series = new BudgeteerSeries();
        series.setName("Target");
        for (int j = 0; j < numberOfWeeks; j++) {
            series.getValues().add(random.nextDouble());
        }
        targetAndActual.setTargetSeries(series);

        return targetAndActual;

    }
}
