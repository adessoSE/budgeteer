package org.wickedsource.budgeteer.web.people;

import org.wickedsource.budgeteer.web.base.BasePage;
import org.wickedsource.budgeteer.web.components.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.dashboard.DashboardPage;

public class PeopleOverviewPage extends BasePage {

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
    }

}
