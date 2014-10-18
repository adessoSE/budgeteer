package org.wickedsource.budgeteer.web.usecase.budgets.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.Breadcrumb;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.BudgetBasePage;
import org.wickedsource.budgeteer.web.usecase.budgets.details.component.distributionchart.PeopleDistributionChart;
import org.wickedsource.budgeteer.web.usecase.budgets.details.component.distributionchart.PeopleDistributionChartModel;
import org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel.BudgetHighlightsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel.BudgetHighlightsPanel;
import org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel.BudgetNameModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

@Mount("budgets/details/${id}")
public class BudgetDetailsPage extends BudgetBasePage {

    public BudgetDetailsPage(PageParameters parameters) {
        super(parameters);
        add(new BudgetHighlightsPanel("highlightsPanel", new BudgetHighlightsModel(getBudgetId())));
        add(new PeopleDistributionChart("distributionChart", new PeopleDistributionChartModel(getBudgetId()), new BudgeteerChartTheme()));
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(BudgetDetailsPage.class, getPageParameters(), new BudgetNameModel(getBudgetId())));
        return model;
    }

}
