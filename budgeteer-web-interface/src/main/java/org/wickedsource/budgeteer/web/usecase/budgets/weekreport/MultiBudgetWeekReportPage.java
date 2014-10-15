package org.wickedsource.budgeteer.web.usecase.budgets.weekreport;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.component.aggregatedrecordtable.AggregatedRecordTable;
import org.wickedsource.budgeteer.web.component.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.component.targetactualchart.TargetAndActualChartOptions;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.component.targetactualchart.MultiBudgetsWeeklyAggregationModel;
import org.wickedsource.budgeteer.web.usecase.budgets.component.weekreporttable.MultiBudgetsWeeklyAggregatedRecordsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("budgets/weeks")
public class MultiBudgetWeekReportPage extends BasePage {

    public MultiBudgetWeekReportPage(PageParameters parameters) {
        super(parameters);

        IModel<TargetAndActual> model = new MultiBudgetsWeeklyAggregationModel(model(from(BudgeteerSession.get().getBudgetFilter())));
        add(new TargetAndActualChart("targetAndActualChart", model, new BudgeteerChartTheme(), TargetAndActualChartOptions.Mode.WEEKLY));

        IModel<List<AggregatedRecord>> tableModel = new MultiBudgetsWeeklyAggregatedRecordsModel(model(from(BudgeteerSession.get().getBudgetFilter())));
        add(new AggregatedRecordTable("table", tableModel));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class, MultiBudgetWeekReportPage.class);
    }
}
