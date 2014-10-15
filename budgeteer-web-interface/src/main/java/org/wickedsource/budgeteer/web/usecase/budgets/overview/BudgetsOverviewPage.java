package org.wickedsource.budgeteer.web.usecase.budgets.overview;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.filter.BudgetTagFilterPanel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.filter.BudgetTagsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.table.BudgetOverviewTable;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.table.FilteredBudgetModel;
import org.wickedsource.budgeteer.web.usecase.budgets.weekreport.MultiBudgetWeekReportPage;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("budgets")
public class BudgetsOverviewPage extends BasePage {

    public BudgetsOverviewPage() {
        BudgetTagsModel tagsModel = new BudgetTagsModel(BudgeteerSession.get().getLoggedInUserId());
        if (BudgeteerSession.get().getBudgetFilter() == null) {
            BudgetTagFilter filter = new BudgetTagFilter();
            filter.setSelectedTags(tagsModel.getObject());
            BudgeteerSession.get().setBudgetFilter(filter);
        }
        add(new BudgetTagFilterPanel("tagFilter", tagsModel, model(from(BudgeteerSession.get().getBudgetFilter()))));
        add(new BudgetOverviewTable("budgetTable", new FilteredBudgetModel(BudgeteerSession.get().getLoggedInUserId(), model(from(BudgeteerSession.get().getBudgetFilter())))));

        add(new BookmarkablePageLink<MultiBudgetWeekReportPage>("weekReportLink1", MultiBudgetWeekReportPage.class));
        add(new BookmarkablePageLink<MultiBudgetWeekReportPage>("weekReportLink2", MultiBudgetWeekReportPage.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
    }

}
