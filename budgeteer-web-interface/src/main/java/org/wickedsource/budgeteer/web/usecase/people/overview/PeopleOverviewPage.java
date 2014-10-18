package org.wickedsource.budgeteer.web.usecase.people.overview;

import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.overview.component.PeopleModel;
import org.wickedsource.budgeteer.web.usecase.people.overview.component.PeopleOverviewTable;

@Mount("people")
public class PeopleOverviewPage extends BasePage {

    public PeopleOverviewPage() {
        PeopleModel model = new PeopleModel(BudgeteerSession.get().getLoggedInUserId());
        PeopleOverviewTable table = new PeopleOverviewTable("peopleOverviewTable", model);
        add(table);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
    }

}
