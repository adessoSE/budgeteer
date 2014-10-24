package org.wickedsource.budgeteer.web.pages.base.basepage;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.BudgeteerReferences;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsPanel;
import org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice.BudgetUnitChoice;
import org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice.BudgetUnitModel;
import org.wickedsource.budgeteer.web.pages.base.basepage.notifications.NotificationDropdown;
import org.wickedsource.budgeteer.web.pages.base.basepage.notifications.NotificationModel;

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
        long projectId = ((BudgeteerSession) getSession()).getProjectId();
        add(breadcrumbs);
        add(new NotificationDropdown("notificationDropdown", new NotificationModel(projectId)));
        add(new BudgetUnitChoice("budgetUnitDropdown", new BudgetUnitModel(projectId)));
        add(new BookmarkablePageLink<ProjectAdministrationPage>("administrationLink", ProjectAdministrationPage.class));
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
