package org.wickedsource.budgeteer.web.components.targetactualchart;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.statistics.BudgeteerSeries;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartUtils;

public class TargetAndActualChartOptions extends Options {

    public enum Mode {
        MONTHLY,

        WEEKLY;
    }

    public TargetAndActualChartOptions(IModel<TargetAndActual> model, Mode mode) {
        setChart(new ChartOptions()
                .setType(SeriesType.COLUMN)
                .setHeight(300));

        setxAxis(new Axis()
                .setLabels(new Labels()
                        .setRotation(-70)));

        switch (mode) {
            case MONTHLY:
                getxAxis().get(0).setCategories(ChartUtils.getMonthLabels(12));
                break;
            case WEEKLY:
                getxAxis().get(0).setCategories(ChartUtils.getWeekLabels(12, PropertyLoader.getProperty(TargetAndActualChart.class, "chart.weekLabelFormat")));
        }

        setyAxis(new Axis()
                .setTitle(new Title("")));

        setPlotOptions(new PlotOptionsChoice()
                .setColumn(new PlotOptions()
                        .setStacking(Stacking.NORMAL))
                .setLine(new PlotOptions()
                        .setColor(new HexColor("#FF0000"))
                        .setMarker(new Marker()
                                .setEnabled(false))));

        setTooltip(new Tooltip()
                .setShared(true));

        if (model.getObject() != null) {
            for (BudgeteerSeries series : model.getObject().getActualSeries()) {
                addSeries(new Series<Double>() {
                }
                        .setName(series.getName())
                        .setData(MoneyUtil.toDouble(series.getValues(), BudgeteerSession.get().getSelectedBudgetUnit())));
            }

            addSeries(new Series<Double>() {
            }
                    .setName("Plan")
                    .setData(MoneyUtil.toDouble(model.getObject().getTargetSeries().getValues(), BudgeteerSession.get().getSelectedBudgetUnit()))
                    .setType(SeriesType.LINE));
        }


    }
}
