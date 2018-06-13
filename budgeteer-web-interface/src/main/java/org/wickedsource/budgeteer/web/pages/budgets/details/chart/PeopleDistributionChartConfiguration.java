package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.charts.ChartStyling;

import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.*;
import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

public class PeopleDistributionChartConfiguration extends ChartConfiguration
		implements Serializable {

	public PeopleDistributionChartConfiguration(PeopleDistributionChartModel model) {
		// Get ModelData
		List<DoubleValue> data =
				model
						.getObject()
						.stream()
						.map(
								share ->
										new DoubleValue(
												MoneyUtil.toDouble(
														share.getShare(), BudgeteerSession.get().getSelectedBudgetUnit())))
						.collect(Collectors.toList());

		List<TextLabel> labels =
				model
						.getObject()
						.stream()
						.map(share -> new TextLabel(share.getName()))
						.collect(Collectors.toList());

		// Get default Colors
		List<RgbColor> defaultColors = ChartStyling.getColors();

		// Add needed colors by rotating
		List<RgbColor> chartColors = new ArrayList<RgbColor>(labels.size());

		for (int i = 0; i < labels.size(); i++) {
			chartColors.add(defaultColors.get(i % defaultColors.size()));
		}

		// Set Chart Configuration

		setType(ChartType.DOUGHNUT);

		setData(
				new Data()
						.setDatasets(Arrays.asList(new Dataset().setData(data).setBackgroundColor(chartColors)))
						.setLabels(labels));

		setOptions(
				new Options()
						.setResponsive(true)
						.setMaintainAspectRatio(false)
						.setLegend(new Legend().setDisplay(true).setPosition(Position.RIGHT)));
	}
}
