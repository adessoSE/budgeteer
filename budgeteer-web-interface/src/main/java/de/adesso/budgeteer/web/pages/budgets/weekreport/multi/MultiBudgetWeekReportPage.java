package de.adesso.budgeteer.web.pages.budgets.weekreport.multi;

import de.adesso.budgeteer.service.record.AggregatedRecord;
import de.adesso.budgeteer.service.statistics.TargetAndActual;
import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import de.adesso.budgeteer.web.components.links.NetGrossLink;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsWeeklyAggregationModel;
import de.adesso.budgeteer.web.pages.budgets.components.weekreporttable.BudgetsWeeklyAggregatedRecordsModel;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.wicketstuff.lazymodel.LazyModel;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("budgets/weeks")
public class MultiBudgetWeekReportPage extends BasePage {

    public MultiBudgetWeekReportPage(PageParameters parameters) {
        super(parameters);

        IModel<TargetAndActual> model = new BudgetsWeeklyAggregationModel(model(LazyModel.from(BudgeteerSession.get().getBudgetFilter())));
        add(new TargetAndActualChart("targetAndActualChart", model, TargetAndActualChartConfiguration.Mode.WEEKLY));

        IModel<List<AggregatedRecord>> tableModel = new BudgetsWeeklyAggregatedRecordsModel(model(LazyModel.from(BudgeteerSession.get().getBudgetFilter())));
        add(new AggregatedRecordTable("table", tableModel));

        add(new NetGrossLink("netGrossLink"));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class, MultiBudgetWeekReportPage.class);
    }

}
