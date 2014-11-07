package org.wickedsource.budgeteer.web.pages.person.details.chart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class BudgetDistributionChart extends Chart {

    public BudgetDistributionChart(String id, BudgetDistributionChartModel model, Theme theme) {
        super(id, new BudgetDistributionChartOptions(model), theme);
    }

}
