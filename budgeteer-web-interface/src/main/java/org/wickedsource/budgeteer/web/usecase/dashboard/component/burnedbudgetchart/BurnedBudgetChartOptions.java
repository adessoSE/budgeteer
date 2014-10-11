package org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.wickedcharts.ChartUtils;

public class BurnedBudgetChartOptions extends Options {

    private BurnedBudgetChartModel model;

    public BurnedBudgetChartOptions(BurnedBudgetChartModel model) {
        this.model = model;

        setChart(new ChartOptions()
                .setType(SeriesType.COLUMN)
                .setHeight(300));

        setxAxis(new Axis()
                .setCategories(ChartUtils.getWeekLabels(model.getNumberOfWeeks(), PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.weekLabelFormat"))));

        setyAxis(new Axis()
                .setTitle(new Title("")));

        addSeries(new Series<Double>() {
        }
                .setData(model.getObject())
                .setName(PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.seriesName")));

    }

}
