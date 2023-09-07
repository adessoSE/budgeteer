package org.wickedsource.budgeteer.web.pages.budgets.weekreport.multi;

import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import org.wickedsource.budgeteer.web.components.links.NetGrossLink;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsWeeklyAggregationModel;
import org.wickedsource.budgeteer.web.pages.budgets.components.weekreporttable.BudgetsWeeklyAggregatedRecordsModel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("budgets/weeks")
public class MultiBudgetWeekReportPage extends BasePage {

  public MultiBudgetWeekReportPage(PageParameters parameters) {
    super(parameters);

    IModel<TargetAndActual> model =
        new BudgetsWeeklyAggregationModel(Model.of(BudgeteerSession.get().getBudgetFilter()));
    add(
        new TargetAndActualChart(
            "targetAndActualChart", model, TargetAndActualChartConfiguration.Mode.WEEKLY));

    IModel<List<AggregatedRecord>> tableModel =
        new BudgetsWeeklyAggregatedRecordsModel(Model.of(BudgeteerSession.get().getBudgetFilter()));
    add(new AggregatedRecordTable("table", tableModel));

    add(new NetGrossLink("netGrossLink"));
  }

  @Override
  @SuppressWarnings("unchecked")
  protected BreadcrumbsModel getBreadcrumbsModel() {
    return new BreadcrumbsModel(
        DashboardPage.class, BudgetsOverviewPage.class, MultiBudgetWeekReportPage.class);
  }
}
