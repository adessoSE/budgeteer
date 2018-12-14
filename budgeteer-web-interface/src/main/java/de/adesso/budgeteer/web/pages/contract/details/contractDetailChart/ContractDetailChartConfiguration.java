package de.adesso.budgeteer.web.pages.contract.details.contractDetailChart;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.web.PropertyLoader;
import de.adesso.budgeteer.web.charts.ChartStyling;
import de.adesso.budgeteer.web.charts.ChartUtils;
import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.*;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ContractDetailChartConfiguration extends ChartConfiguration implements Serializable {
	
    public ContractDetailChartConfiguration(ContractDetailChartModel model) {
    	setType(ChartType.BAR);
    	
    	setOptions(ChartStyling.getOptions());
    	
    	setOptionalJavascript(new ArrayList<>());
		addOptionalJavascript(ChartStyling.readFile("dataLabellingPlugin.js"));
    	
    	getOptions().setTooltips(new Tooltips()
				.setIntersect(true)
				.setMode(TooltipMode.INDEX));
    	
    	getOptions().getLayout().getPadding().setTop(25);

    	Dataset remainingTotalBudget = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.remainingBudget"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getRemainingTotalBudget())))
    			.setBackgroundColor(ChartStyling.getColors().get(0));

    	Dataset burnedMoneyAllBudget = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.burnedBudget"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getBurnedMoneyAllBudgets())))
    			.setBackgroundColor(ChartStyling.getColors().get(1));

    	Dataset burnedMoneyInvoice = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.invoice"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getBurnedMoneyInvoice())))
    			.setBackgroundColor(ChartStyling.getColors().get(2));
    	
    	setData(new Data()
    			.setLabels(TextLabel.of(ChartUtils.getMonthLabels(model.getNumberOfMonths())))
    			.setDatasets(Arrays.asList(remainingTotalBudget,burnedMoneyAllBudget,burnedMoneyInvoice)));
    	
    }
}
