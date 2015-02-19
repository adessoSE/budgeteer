package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class PeopleDistributionChart extends Chart {

    private PeopleDistributionChartModel model;

    public PeopleDistributionChart(String id, PeopleDistributionChartModel model, Theme theme) {
        super(id, new PeopleDistributionChartOptions(model), theme);
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setOptions(new PeopleDistributionChartOptions(model));
    }

}
