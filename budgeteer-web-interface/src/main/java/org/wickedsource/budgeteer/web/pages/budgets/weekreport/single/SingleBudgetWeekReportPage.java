package org.wickedsource.budgeteer.web.pages.budgets.weekreport.single;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.AggregatedWorkingRecord;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChartOptions;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetBasePage;
import org.wickedsource.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsWeeklyAggregationModel;
import org.wickedsource.budgeteer.web.pages.budgets.components.weekreporttable.BudgetsWeeklyAggregatedRecordsModel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

import java.util.List;

@Mount("budgets/weeks/${id}")
public class SingleBudgetWeekReportPage extends BudgetBasePage {

    public SingleBudgetWeekReportPage(PageParameters parameters) {
        super(parameters);

        IModel<TargetAndActual> model = new BudgetsWeeklyAggregationModel(getBudgetId());
        add(new TargetAndActualChart("targetAndActualChart", model, new BudgeteerChartTheme(), TargetAndActualChartOptions.Mode.WEEKLY));

        IModel<List<AggregatedWorkingRecord>> tableModel = new BudgetsWeeklyAggregatedRecordsModel(getBudgetId());
        add(new AggregatedRecordTable("table", tableModel));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class, SingleBudgetWeekReportPage.class);
    }
}
