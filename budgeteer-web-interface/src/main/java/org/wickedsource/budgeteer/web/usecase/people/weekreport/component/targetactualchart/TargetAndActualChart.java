package org.wickedsource.budgeteer.web.usecase.people.weekreport.component.targetactualchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

public class TargetAndActualChart extends Chart {

    public TargetAndActualChart(String id, IModel<TargetAndActual> model, Theme theme) {
        super(id, new TargetAndActualChartOptions(model), theme);
    }

}
