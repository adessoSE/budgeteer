package org.wickedsource.budgeteer.web.pages.contract.budgetOverview;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.table.BudgetOverviewTable;
import org.wickedsource.budgeteer.web.pages.budgets.overview.table.FilteredBudgetModelByContract;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("budgetsForContract")
public class BudgetForContractOverviewPage extends BasePage {

    public BudgetForContractOverviewPage(PageParameters pageParameters) {
        super(pageParameters);
        add(new BudgetOverviewTable("budgetTable", new FilteredBudgetModelByContract(getParameterId()), getBreadcrumbsModel()));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetForContractOverviewPage.class);
    }

}
