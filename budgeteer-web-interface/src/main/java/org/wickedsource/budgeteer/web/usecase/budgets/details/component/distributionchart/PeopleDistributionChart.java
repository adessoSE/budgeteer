package org.wickedsource.budgeteer.web.usecase.budgets.details.component.distributionchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class PeopleDistributionChart extends Chart {

    public PeopleDistributionChart(String id, PeopleDistributionChartModel model, Theme theme) {
        super(id, new PeopleDistributionChartOptions(model), theme);
    }

}
