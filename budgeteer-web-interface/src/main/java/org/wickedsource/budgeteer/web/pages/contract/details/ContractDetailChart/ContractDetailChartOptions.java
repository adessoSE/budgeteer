package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;


import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartUtils;

public class ContractDetailChartOptions extends Options {

    public ContractDetailChartOptions(ContractDetailChartModel model) {
        setChart(new ChartOptions()
                .setType(SeriesType.COLUMN)
                .setHeight(300));

        setxAxis(new Axis()
                .setCategories(ChartUtils.getMonthLabels(model.getNumberOfMonths())));

        setyAxis(new Axis()
                .setTitle(new Title("")));

        addSeries(new Series<Double>() {
        }
                .setData(MoneyUtil.toDouble(model.getObject().getRemainingTotalBudget(), BudgeteerSession.get().getSelectedBudgetUnit()))
                .setName(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.remainingBudget")));

        addSeries(new Series<Double>() {
        }
                .setData(MoneyUtil.toDouble(model.getObject().getBurnedMoneyAllBudgets(), BudgeteerSession.get().getSelectedBudgetUnit()))
                .setName(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.burnedBudget")));

        addSeries(new Series<Double>() {
        }
                .setData(MoneyUtil.toDouble(model.getObject().getBurnedMoneyInvoice(), BudgeteerSession.get().getSelectedBudgetUnit()))
                .setName(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.invoice")));

    }
}
