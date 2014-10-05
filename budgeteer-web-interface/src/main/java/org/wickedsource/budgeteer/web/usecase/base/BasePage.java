package org.wickedsource.budgeteer.web.usecase.base;

import org.apache.wicket.markup.html.WebPage;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsPanel;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitDropdown;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitModel;
import org.wickedsource.budgeteer.web.usecase.base.component.notification.NotificationDropdown;
import org.wickedsource.budgeteer.web.usecase.base.component.notification.NotificationModel;

public abstract class BasePage extends WebPage {

    @SuppressWarnings("unchecked")
    public BasePage() {
        BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbsPanel", getBreadcrumbsModel());
        long loggedInUserId = ((BudgeteerSession) getSession()).getLoggedInUserId();
        add(breadcrumbs);
        add(new NotificationDropdown("notificationDropdown", new NotificationModel(loggedInUserId)));
        add(new BudgetUnitDropdown("budgetUnitDropdown", new BudgetUnitModel(loggedInUserId)));
    }

    /**
     * Expects a BreadcrumbsModel to be returned that contains all pages that should be displayed in the breadcrumbs.
     */
    protected abstract BreadcrumbsModel getBreadcrumbsModel();
}
