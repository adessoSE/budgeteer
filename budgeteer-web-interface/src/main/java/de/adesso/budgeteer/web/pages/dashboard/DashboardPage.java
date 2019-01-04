package de.adesso.budgeteer.web.pages.dashboard;

import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import de.adesso.budgeteer.web.pages.contract.overview.ContractOverviewPage;
import de.adesso.budgeteer.web.pages.dashboard.burnedbudgetchart.BurnedBudgetChart;
import de.adesso.budgeteer.web.pages.dashboard.burnedbudgetchart.BurnedBudgetChartModel;
import de.adesso.budgeteer.web.pages.dashboard.dailyratechart.AverageDailyRateChart;
import de.adesso.budgeteer.web.pages.dashboard.dailyratechart.AverageDailyRateChartModel;
import de.adesso.budgeteer.web.pages.hours.HoursPage;
import de.adesso.budgeteer.web.pages.imports.ImportsOverviewPage;
import de.adesso.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;
import de.adesso.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import de.adesso.budgeteer.web.pages.templates.TemplatesPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

@Mount("dashboard")
public class DashboardPage extends BasePage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DashboardPage() {
        BurnedBudgetChartModel burnedBudgetModel = new BurnedBudgetChartModel(BudgeteerSession.get().getProjectId(), 8);
        add(new BurnedBudgetChart("burnedBudgetChart", burnedBudgetModel));
        
        add(new Label("username", new UsernameModel()));

        add(new Label("projectname", new ProjectnameModel()));

        AverageDailyRateChartModel avgDailyRateModel = new AverageDailyRateChartModel(BudgeteerSession.get().getProjectId(), 30);
        add(new AverageDailyRateChart("averageDailyRateChart", avgDailyRateModel));
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
