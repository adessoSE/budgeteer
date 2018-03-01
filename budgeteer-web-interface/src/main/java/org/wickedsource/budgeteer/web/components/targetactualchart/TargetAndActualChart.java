package org.wickedsource.budgeteer.web.components.targetactualchart;


import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

import de.adesso.wickedcharts.wicket7.chartjs.Chart;

public class TargetAndActualChart extends Chart {

    private IModel<TargetAndActual> model;

    private TargetAndActualChartConfiguration.Mode mode;

    public TargetAndActualChart(String id, IModel<TargetAndActual> model, TargetAndActualChartConfiguration.Mode mode) {
        super(id, new TargetAndActualChartConfiguration(model, mode));
        this.model = model;
        this.mode = mode;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        // resetting options to force re-rendering with new parameters
        setChartConfiguration(new TargetAndActualChartConfiguration(model, mode));
    }
}
