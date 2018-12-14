package de.adesso.budgeteer.web.pages.contract.budgetOverview;

import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.budgets.overview.table.BudgetOverviewTable;
import de.adesso.budgeteer.web.pages.budgets.overview.table.FilteredBudgetModelByContract;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.pages.contract.details.ContractDetailsPage;
import de.adesso.budgeteer.web.pages.contract.overview.ContractOverviewPage;

@Mount("/contracts/details/budgets/${id}")
public class BudgetForContractOverviewPage extends BasePage {

    public BudgetForContractOverviewPage(PageParameters pageParameters) {
        super(pageParameters);
        add(new BudgetOverviewTable("budgetTable", new FilteredBudgetModelByContract(getParameterId()), getBreadcrumbsModel()));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model =  new BreadcrumbsModel(DashboardPage.class, ContractOverviewPage.class);
        model.addBreadcrumb(ContractDetailsPage.class, getPageParameters());
        model.addBreadcrumb(BudgetForContractOverviewPage.class, getPageParameters());
        return model;
    }
}
