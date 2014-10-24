package org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class BurnedBudgetChart extends Chart {

    private BurnedBudgetChartModel model;

    public BurnedBudgetChart(String id, BurnedBudgetChartModel model, Theme theme) {
        super(id, new BurnedBudgetChartOptions(model), theme);
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setOptions(new BurnedBudgetChartOptions(model));
    }

}
