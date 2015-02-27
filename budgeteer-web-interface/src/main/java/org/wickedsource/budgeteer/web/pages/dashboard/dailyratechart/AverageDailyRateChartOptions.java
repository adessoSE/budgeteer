package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.apache.wicket.injection.Injector;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
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
                .setData(MoneyUtil.toDouble(model.getObject(), BudgeteerSession.get().getSelectedBudgetUnit()))
                .setPointInterval(24 * 60 * 60 * 1000)
                .setPointStart(getStartTimestamp(model.getNumberOfDays()))
                .setName(PropertyLoader.getProperty(AverageDailyRateChart.class, "chart.seriesName")));

    }

    private Number getStartTimestamp(int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -numberOfDays+1); // without the +1 the chart would be off by one day -> Issue #16
        return c.getTimeInMillis();
    }

}
