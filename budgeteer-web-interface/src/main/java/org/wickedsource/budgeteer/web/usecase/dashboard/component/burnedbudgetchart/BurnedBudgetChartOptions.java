package org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.wickedsource.budgeteer.web.PropertyLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BurnedBudgetChartOptions extends Options {

    private BurnedBudgetChartModel model;

    public BurnedBudgetChartOptions(BurnedBudgetChartModel model) {
        this.model = model;

        setChart(new ChartOptions()
                .setType(SeriesType.COLUMN)
                .setHeight(300));

        setxAxis(new Axis()
                .setCategories(getWeekLabels()));

        setyAxis(new Axis()
                .setTitle(new Title("")));

        addSeries(new Series<Double>() {
        }
                .setData(model.getObject())
                .setName(PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.seriesName")));

    }

    private List<String> getWeekLabels() {
        String weekLabelFormat = PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.weekLabelFormat");
        List<String> labels = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        for (int i = 0; i < model.getNumberOfWeeks(); i++) {
            labels.add(String.format(weekLabelFormat, currentWeek));
            c.add(Calendar.WEEK_OF_YEAR, -1);
            currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        }
        Collections.reverse(labels);
        return labels;
    }

}
