package org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class BurnedBudgetChart extends Chart {

    public BurnedBudgetChart(String id, BurnedBudgetChartModel model, Theme theme) {
        super(id, new BurnedBudgetChartOptions(model), theme);
    }

}
