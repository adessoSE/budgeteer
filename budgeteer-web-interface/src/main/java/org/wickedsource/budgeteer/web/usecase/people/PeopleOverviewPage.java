package org.wickedsource.budgeteer.web.usecase.people;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.component.PeopleModel;
import org.wickedsource.budgeteer.web.usecase.people.component.PeopleOverviewTable;

@Component
@Scope("prototype")
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
