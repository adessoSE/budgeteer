package org.wickedsource.budgeteer.web.usecase.people;

import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

public class PersonDetailsPage extends BasePage {

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class, PersonDetailsPage.class);
    }

}
