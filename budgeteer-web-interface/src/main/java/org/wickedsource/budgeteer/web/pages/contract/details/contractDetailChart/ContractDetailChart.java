package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;

import de.adesso.wickedcharts.wicket7.chartjs.Chart;

public class ContractDetailChart extends Chart{

    private ContractDetailChartModel model;

    public ContractDetailChart(String id, ContractDetailChartModel model) {
        super(id, new ContractDetailChartConfiguration(model));
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setChartConfiguration(new ContractDetailChartConfiguration(model));
    }
}
