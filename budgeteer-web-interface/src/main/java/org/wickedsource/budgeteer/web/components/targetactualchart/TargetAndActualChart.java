package org.wickedsource.budgeteer.web.components.targetactualchart;

import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

public class TargetAndActualChart extends Chart {

    private IModel<TargetAndActual> model;

    private TargetAndActualChartOptions.Mode mode;

    public TargetAndActualChart(String id, IModel<TargetAndActual> model, Theme theme, TargetAndActualChartOptions.Mode mode) {
        super(id, new TargetAndActualChartOptions(model, mode), theme);
        this.model = model;
        this.mode = mode;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        // resetting options to force re-rendering with new parameters
        setOptions(new TargetAndActualChartOptions(model, mode));
    }
}
