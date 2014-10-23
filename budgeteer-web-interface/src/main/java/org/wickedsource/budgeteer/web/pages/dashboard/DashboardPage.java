package org.wickedsource.budgeteer.web.pages.dashboard;


import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart.BurnedBudgetChart;
import org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart.BurnedBudgetChartModel;
import org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart.AverageDailyRateChart;
import org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart.AverageDailyRateChartModel;
import org.wickedsource.budgeteer.web.pages.hours.HoursPage;
import org.wickedsource.budgeteer.web.pages.imports.ImportsOverviewPage;
import org.wickedsource.budgeteer.web.pages.people.overview.PeopleOverviewPage;

@Mount("dashboard")
public class DashboardPage extends BasePage {

    public DashboardPage() {
        BudgeteerChartTheme theme = new BudgeteerChartTheme();
        BurnedBudgetChartModel burnedBudgetModel = new BurnedBudgetChartModel(BudgeteerSession.get().getLoggedInUserId(), 8);
        add(new BurnedBudgetChart("burnedBudgetChart", burnedBudgetModel, theme));

        AverageDailyRateChartModel avgDailyRateModel = new AverageDailyRateChartModel(BudgeteerSession.get().getLoggedInUserId(), 30);
        add(new AverageDailyRateChart("averageDailyRateChart", avgDailyRateModel, theme));

        add(new BookmarkablePageLink<PeopleOverviewPage>("peopleLink1", PeopleOverviewPage.class));
        add(new BookmarkablePageLink<PeopleOverviewPage>("peopleLink2", PeopleOverviewPage.class));

        add(new BookmarkablePageLink<HoursPage>("hoursLink1", HoursPage.class));
        add(new BookmarkablePageLink<HoursPage>("hoursLink2", HoursPage.class));

        add(new BookmarkablePageLink<BudgetsOverviewPage>("budgetsLink1", BudgetsOverviewPage.class));
        add(new BookmarkablePageLink<BudgetsOverviewPage>("budgetsLink2", BudgetsOverviewPage.class));

        add(new BookmarkablePageLink<ImportsOverviewPage>("importsLink1", ImportsOverviewPage.class));
        add(new BookmarkablePageLink<ImportsOverviewPage>("importsLink2", ImportsOverviewPage.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class);
    }
}
