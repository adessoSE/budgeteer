package org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.apache.wicket.injection.Injector;
import org.wickedsource.budgeteer.web.PropertyLoader;

import java.util.Calendar;

public class AverageDailyRateChartOptions extends Options {

    public AverageDailyRateChartOptions(AverageDailyRateChartModel model) {
        Injector.get().inject(this);

        setChart(new ChartOptions()
                .setType(SeriesType.LINE)
                .setHeight(300));

        setxAxis(new Axis()
                .setType(AxisType.DATETIME));

        setyAxis(new Axis()
                .setTitle(new Title("")));

        addSeries(new Series<Double>() {
        }
                .setData(model.getObject())
                .setPointInterval(24 * 60 * 60 * 1000)
                .setPointStart(getStartTimestamp(model.getNumberOfDays()))
                .setName(PropertyLoader.getProperty(AverageDailyRateChart.class, "chart.seriesName")));

    }

    private Number getStartTimestamp(int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        return c.getTimeInMillis();
    }

}
