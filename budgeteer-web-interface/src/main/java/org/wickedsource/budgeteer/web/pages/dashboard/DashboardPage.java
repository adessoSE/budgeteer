package org.wickedsource.budgeteer.web.pages.dashboard;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.ContractOverviewPage;
import org.wickedsource.budgeteer.web.pages.hours.HoursPage;
import org.wickedsource.budgeteer.web.pages.imports.ImportsOverviewPage;
import org.wickedsource.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;

@Mount("dashboard")
public class DashboardPage extends BasePage {

  /** */
  private static final long serialVersionUID = 1L;

  @SpringBean private ProjectService projectService;

  public DashboardPage() {
    add(new Label("username", () -> BudgeteerSession.get().getLoggedInUser().getName()));
    add(
        new Label(
            "projectname",
            () -> projectService.findProjectById(BudgeteerSession.get().getProjectId()).getName()));
    add(new BookmarkablePageLink<PeopleOverviewPage>("peopleLink", PeopleOverviewPage.class));
    add(new BookmarkablePageLink<HoursPage>("hoursLink", HoursPage.class));
    add(new BookmarkablePageLink<BudgetsOverviewPage>("budgetsLink", BudgetsOverviewPage.class));
    add(new BookmarkablePageLink<BudgetsOverviewPage>("contractsLink", ContractOverviewPage.class));
    add(new BookmarkablePageLink<InvoiceOverviewPage>("invoiceLink", InvoiceOverviewPage.class));
    add(new BookmarkablePageLink<ImportsOverviewPage>("importsLink", ImportsOverviewPage.class));
    add(new BookmarkablePageLink<ImportsOverviewPage>("templatesLink", TemplatesPage.class));
  }

  @Override
  protected BreadcrumbsModel getBreadcrumbsModel() {
    return new BreadcrumbsModel(DashboardPage.class);
  }
}
