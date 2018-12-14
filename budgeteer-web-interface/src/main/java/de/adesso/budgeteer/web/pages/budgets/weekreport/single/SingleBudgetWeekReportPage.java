package de.adesso.budgeteer.web.pages.budgets.weekreport.single;

import de.adesso.budgeteer.service.budget.BudgetService;
import de.adesso.budgeteer.service.record.AggregatedRecord;
import de.adesso.budgeteer.service.statistics.TargetAndActual;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import de.adesso.budgeteer.web.components.budget.dropdown.BudgetLinkDropdownForm;
import de.adesso.budgeteer.web.components.links.NetGrossLink;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsWeeklyAggregationModel;
import de.adesso.budgeteer.web.pages.budgets.components.weekreporttable.BudgetsWeeklyAggregatedRecordsModel;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;

import javax.inject.Inject;
import java.util.List;

@Mount("budgets/weeks/${id}")
public class SingleBudgetWeekReportPage extends BasePage {

    @Inject
    private BudgetService budgetService;

    public SingleBudgetWeekReportPage(PageParameters parameters) {
        super(parameters);
        add(new Label("budgetName", budgetService.loadBudgetBaseData(getParameterId()).getName()));
        add(new Label("budgetName2", budgetService.loadBudgetBaseData(getParameterId()).getName()));

        IModel<TargetAndActual> model = new BudgetsWeeklyAggregationModel(getParameterId());
        add(new TargetAndActualChart("targetAndActualChart", model, TargetAndActualChartConfiguration.Mode.WEEKLY));

        IModel<List<AggregatedRecord>> tableModel = new BudgetsWeeklyAggregatedRecordsModel(getParameterId());
        add(new AggregatedRecordTable("table", tableModel));

        add(new BudgetLinkDropdownForm("budgetLinkDropdownForm").setLinkType(BudgetLinkDropdownForm.BudgetLinkType.WEEKLY));
        add(new NetGrossLink("netGrossLink"));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class, SingleBudgetWeekReportPage.class);
    }
}
