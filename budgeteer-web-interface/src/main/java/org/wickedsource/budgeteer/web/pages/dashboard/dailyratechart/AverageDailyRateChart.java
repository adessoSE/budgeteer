package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;

public class AverageDailyRateChart extends Chart {
    private AverageDailyRateChartModel model;

    public AverageDailyRateChart(String id, AverageDailyRateChartModel model, Theme theme) {
        super(id, new AverageDailyRateChartOptions(model), theme);
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setOptions(new AverageDailyRateChartOptions(model));
    }

}
