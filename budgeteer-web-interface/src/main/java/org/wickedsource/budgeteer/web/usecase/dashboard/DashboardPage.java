package org.wickedsource.budgeteer.web.usecase.dashboard;


import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart.BurnedBudgetChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart.BurnedBudgetChartModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart.AverageDailyRateChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart.AverageDailyRateChartModel;
import org.wickedsource.budgeteer.web.usecase.hours.HoursPage;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;

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
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class);
    }
}
