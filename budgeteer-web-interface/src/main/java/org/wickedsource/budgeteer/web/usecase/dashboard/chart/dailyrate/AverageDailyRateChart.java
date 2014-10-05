package org.wickedsource.budgeteer.web.usecase.dashboard.chart.dailyrate;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class AverageDailyRateChart extends Chart {

    public AverageDailyRateChart(String id, AverageDailyRateChartModel model, Theme theme) {
        super(id, new AverageDailyRateChartOptions(model), theme);
    }

}
