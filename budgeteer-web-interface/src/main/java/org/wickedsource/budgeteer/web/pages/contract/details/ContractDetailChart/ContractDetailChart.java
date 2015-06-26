package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;


import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class ContractDetailChart extends Chart{

    private ContractDetailChartModel model;

    public ContractDetailChart(String id, ContractDetailChartModel model, Theme theme) {
        super(id, new ContractDetailChartOptions(model), theme);
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setOptions(new ContractDetailChartOptions(model));
    }
}
