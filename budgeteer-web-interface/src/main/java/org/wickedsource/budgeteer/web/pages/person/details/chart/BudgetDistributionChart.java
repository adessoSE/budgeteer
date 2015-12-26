package org.wickedsource.budgeteer.web.pages.person.details.chart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;

public class BudgetDistributionChart extends Chart {
    private BudgetDistributionChartModel model;

    public BudgetDistributionChart(String id, BudgetDistributionChartModel model, Theme theme) {
        super(id, new BudgetDistributionChartOptions(model), theme);
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setOptions(new BudgetDistributionChartOptions(model));
    }

}
