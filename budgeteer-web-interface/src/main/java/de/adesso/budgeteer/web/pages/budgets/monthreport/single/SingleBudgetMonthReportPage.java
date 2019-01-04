package de.adesso.budgeteer.web.pages.budgets.monthreport.single;

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
import de.adesso.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsMonthlyAggregationModel;
import de.adesso.budgeteer.web.pages.budgets.components.weekreporttable.BudgetsMonthlyAggregatedRecordsModel;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;

@Mount("budgets/months/${id}")
public class SingleBudgetMonthReportPage extends BasePage {

    @Inject
    private BudgetService budgetService;

    public SingleBudgetMonthReportPage(PageParameters parameters) {
        super(parameters);

        IModel<TargetAndActual> model = new BudgetsMonthlyAggregationModel(getParameterId());
        add(new Label("budgetName", budgetService.loadBudgetBaseData(getParameterId()).getName()));
        add(new Label("budgetName2", budgetService.loadBudgetBaseData(getParameterId()).getName()));
        add(new TargetAndActualChart("targetAndActualChart", model, TargetAndActualChartConfiguration.Mode.MONTHLY));

        IModel<List<AggregatedRecord>> tableModel = new BudgetsMonthlyAggregatedRecordsModel(getParameterId());
        add(new AggregatedRecordTable("table", tableModel));

        add(new BudgetLinkDropdownForm("budgetLinkDropdownForm").setLinkType(BudgetLinkDropdownForm.BudgetLinkType.MONTHLY));
        add(new NetGrossLink("netGrossLink"));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class, SingleBudgetMonthReportPage.class);
    }
}
