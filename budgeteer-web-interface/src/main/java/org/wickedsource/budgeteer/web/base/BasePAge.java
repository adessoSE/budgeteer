package org.wickedsource.budgeteer.web.base;

import org.apache.wicket.markup.html.WebPage;
import org.wickedsource.budgeteer.web.components.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.components.breadcrumb.BreadcrumbsPanel;
import org.wickedsource.budgeteer.web.components.notification.NotificationDropdown;
import org.wickedsource.budgeteer.web.components.notification.NotificationModel;

public abstract class BasePage extends WebPage {

    @SuppressWarnings("unchecked")
    public BasePage() {
        BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbsPanel", getBreadcrumbsModel());
        long loggedInUserId = ((BudgeteerSession) getSession()).getLoggedInUserId();
        add(breadcrumbs);
        add(new NotificationDropdown("notificationDropdown", new NotificationModel(loggedInUserId)));
    }

    /**
     * Expects a BreadcrumbsModel to be returned that contains all pages that should be displayed in the breadcrumbs.
     */
    protected abstract BreadcrumbsModel getBreadcrumbsModel();
}
