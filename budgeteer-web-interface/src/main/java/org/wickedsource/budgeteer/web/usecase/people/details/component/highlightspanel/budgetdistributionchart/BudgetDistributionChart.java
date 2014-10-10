package org.wickedsource.budgeteer.web.usecase.people.details.component.highlightspanel.budgetdistributionchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class BudgetDistributionChart extends Chart {

    public BudgetDistributionChart(String id, BudgetDistributionChartModel model, Theme theme) {
        super(id, new BudgetDistributionChartOptions(model), theme);
    }

}
