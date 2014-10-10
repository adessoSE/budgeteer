package org.wickedsource.budgeteer.web.usecase.people.details.component.budgetdistributionchart;

import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PixelOrPercent;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.wickedsource.budgeteer.service.statistics.BudgetShare;

import java.util.Arrays;

public class BudgetDistributionChartOptions extends Options {

    public BudgetDistributionChartOptions(BudgetDistributionChartModel model) {

        setChart(new ChartOptions()
                .setHeight(200)
                .setSpacingBottom(0)
                .setSpacingTop(0)
                .setSpacingLeft(0)
                .setSpacingRight(0)
                .setMargin(Arrays.asList(0, 0, 0, 0)));

        Series<Point> series = new PointSeries()
                .setType(SeriesType.PIE)
                .setInnerSize(new PixelOrPercent(50, PixelOrPercent.Unit.PERCENT));

        for (BudgetShare share : model.getObject()) {
            series.addPoint(new Point(share.getBudgetName(), share.getShare()));
        }

        addSeries(series);
    }
}
