package org.wickedsource.budgeteer.web.dashboard;

import org.wickedsource.budgeteer.web.base.BasePage;
import org.wickedsource.budgeteer.web.components.breadcrumb.BreadcrumbsModel;

public class DashboardPage extends BasePage {

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class);
    }
}
