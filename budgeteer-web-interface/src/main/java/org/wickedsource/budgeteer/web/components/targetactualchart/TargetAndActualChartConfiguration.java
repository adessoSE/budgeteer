package org.wickedsource.budgeteer.web.components.targetactualchart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.statistics.MoneySeries;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartStyling;
import org.wickedsource.budgeteer.web.charts.ChartUtils;

import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.*;
import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;
import de.adesso.wickedcharts.chartjs.chartoptions.colors.SimpleColor;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

public class TargetAndActualChartConfiguration extends ChartConfiguration implements Serializable {

	public enum Mode {
		MONTHLY,

		WEEKLY
	}

	public TargetAndActualChartConfiguration(IModel<TargetAndActual> model, Mode mode) {
		setType(ChartType.STACKED_BAR);
		List<String> labels = null;
		switch (mode) {
		case MONTHLY:
			labels = ChartUtils.getMonthLabels(12);
			break;
		case WEEKLY:
			labels = ChartUtils.getWeekLabels(12,
					PropertyLoader.getProperty(TargetAndActualChart.class, "chart.weekLabelFormat"));
		}

		List<Dataset> datasets = null;
		List<RgbColor> colorList = ChartStyling.getColors();

		if (model.getObject() != null) {
			datasets = new ArrayList<Dataset>(model.getObject().getActualSeries().size());
			for (int i = 0; i < model.getObject().getActualSeries().size(); i++) {
				MoneySeries series = model.getObject().getActualSeries().get(i);
				Dataset newDataset = new Dataset().setLabel(series.getName())
						.setData(DoubleValue.of(
								MoneyUtil.toDouble(series.getValues(), BudgeteerSession.get().getSelectedBudgetUnit())))
						.setBackgroundColor(colorList.get(i % colorList.size()));
				datasets.add(newDataset);
			}

			Dataset planDataset = new Dataset()
					.setLabel("Plan")
					.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getTargetSeries().getValues(),
			BudgeteerSession.get().getSelectedBudgetUnit())))
					.setFill(false)
					.setType(ChartType.LINE)
					.setBackgroundColor(SimpleColor.RED)
					.setBorderColor(SimpleColor.RED);
			datasets.add(planDataset);
		}

		setData(new Data().setDatasets(datasets).setLabels(TextLabel.of(labels)));

		setOptions(new Options()
				.setMaintainAspectRatio(false)
				.setResponsive(true)
				.setTooltips(new Tooltips()
						.setIntersect(true)
						.setMode(TooltipMode.INDEX))
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(new Scales()
						.setXAxes(new AxesScale()
								.setStacked(true)
								.setGridLines(new GridLines()
										.setDisplay(false)
										.setDrawBorder(false)))
						.setYAxes(new AxesScale()
								.setStacked(true)
								.setGridLines(new GridLines()
										.setDrawBorder(false)))));

	}
}
