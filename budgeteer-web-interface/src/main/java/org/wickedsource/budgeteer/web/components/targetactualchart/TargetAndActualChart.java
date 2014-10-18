package org.wickedsource.budgeteer.web.components.targetactualchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

public class TargetAndActualChart extends Chart {

    public TargetAndActualChart(String id, IModel<TargetAndActual> model, Theme theme, TargetAndActualChartOptions.Mode mode) {
        super(id, new TargetAndActualChartOptions(model, mode), theme);
    }

}
