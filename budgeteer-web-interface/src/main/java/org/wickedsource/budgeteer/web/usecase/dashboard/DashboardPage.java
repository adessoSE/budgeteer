package org.wickedsource.budgeteer.web.usecase.dashboard;


import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.BudgeteerSession;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.chart.budgetburned.BurnedBudgetChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.chart.budgetburned.BurnedBudgetChartModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.chart.dailyrate.AverageDailyRateChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.chart.dailyrate.AverageDailyRateChartModel;
import org.wickedsource.budgeteer.web.usecase.people.PeopleOverviewPage;
import org.wickedsource.budgeteer.web.wickedcharts.BudgeteerChartTheme;

public class DashboardPage extends BasePage {

    public DashboardPage() {
        add(new BookmarkablePageLink<Void>("peopleLink1", PeopleOverviewPage.class));
        add(new BookmarkablePageLink<Void>("peopleLink2", PeopleOverviewPage.class));

        BudgeteerChartTheme theme = new BudgeteerChartTheme();
        BurnedBudgetChartModel burnedBudgetModel = new BurnedBudgetChartModel(BudgeteerSession.get().getLoggedInUserId(), 8);
        add(new BurnedBudgetChart("burnedBudgetChart", burnedBudgetModel, theme));

        AverageDailyRateChartModel avgDailyRateModel = new AverageDailyRateChartModel(BudgeteerSession.get().getLoggedInUserId(), 30);
        add(new AverageDailyRateChart("averageDailyRateChart", avgDailyRateModel, theme));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class);
    }
}
