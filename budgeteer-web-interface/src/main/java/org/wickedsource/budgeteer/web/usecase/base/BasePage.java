package org.wickedsource.budgeteer.web.usecase.base;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.BudgeteerReferences;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsPanel;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitDropdown;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitModel;
import org.wickedsource.budgeteer.web.usecase.base.component.notification.NotificationDropdown;
import org.wickedsource.budgeteer.web.usecase.base.component.notification.NotificationModel;

public abstract class BasePage extends WebPage {

    public BasePage(PageParameters parameters) {
        super(parameters);
        addComponents();
    }

    public BasePage() {
        addComponents();
    }

    @SuppressWarnings("unchecked")
    private void addComponents() {
        BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbsPanel", getBreadcrumbsModel());
        long loggedInUserId = ((BudgeteerSession) getSession()).getLoggedInUserId();
        add(breadcrumbs);
        add(new NotificationDropdown("notificationDropdown", new NotificationModel(loggedInUserId)));
        add(new BudgetUnitDropdown("budgetUnitDropdown", new BudgetUnitModel(loggedInUserId)));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
    }

    /**
     * Expects a BreadcrumbsModel to be returned that contains all pages that should be displayed in the breadcrumbs.
     */
    protected abstract BreadcrumbsModel getBreadcrumbsModel();

}
