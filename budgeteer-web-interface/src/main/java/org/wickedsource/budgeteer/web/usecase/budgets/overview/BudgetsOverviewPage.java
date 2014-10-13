package org.wickedsource.budgeteer.web.usecase.budgets.overview;

import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.filter.BudgetTagFilterPanel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.filter.BudgetTagsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("budgets")
public class BudgetsOverviewPage extends BasePage {

    public BudgetsOverviewPage() {
        BudgetTagsModel tagsModel = new BudgetTagsModel(BudgeteerSession.get().getLoggedInUserId());
        BudgetTagFilter filter = new BudgetTagFilter();
        filter.setSelectedTags(tagsModel.getObject());
        add(new BudgetTagFilterPanel("tagFilter", tagsModel, model(from(filter))));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
    }

}
