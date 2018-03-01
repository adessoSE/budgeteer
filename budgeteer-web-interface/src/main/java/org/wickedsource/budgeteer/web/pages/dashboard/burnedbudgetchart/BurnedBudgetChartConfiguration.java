package org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartStyling;
import org.wickedsource.budgeteer.web.charts.ChartUtils;

import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.ChartType;
import de.adesso.wickedcharts.chartjs.chartoptions.Data;
import de.adesso.wickedcharts.chartjs.chartoptions.Dataset;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

public class BurnedBudgetChartConfiguration extends ChartConfiguration implements Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BurnedBudgetChartConfiguration(BurnedBudgetChartModel model) {
    	setType(ChartType.BAR);
		setOptions(ChartStyling.getOptions());
		
    	List<String> labelList = ChartUtils.getWeekLabels(model.getNumberOfWeeks(), PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.weekLabelFormat"));

    	Dataset dataset = new Dataset()
    			.setBackgroundColor(ChartStyling.getColors().get(0))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject(), BudgeteerSession.get().getSelectedBudgetUnit())))
    			.setLabel(PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.seriesName"));
    	
    	setData(new Data()
    			.setLabels(TextLabel.of(labelList))
    			.setDatasets(Arrays.asList(dataset))
    			);
    	
    	
		getOptions().getScales().getYAxes().get(0).getTicks()
						.setBeginAtZero(true)
						.setSuggestedMin(0)
						.setMaxTicksLimit(5);
    }
}
