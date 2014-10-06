package org.wickedsource.budgeteer.web.usecase.dashboard;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.budgetburnedchart.BurnedBudgetChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.budgetburnedchart.BurnedBudgetChartModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart.AverageDailyRateChart;
import org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart.AverageDailyRateChartModel;
import org.wickedsource.budgeteer.web.wickedcharts.BudgeteerChartTheme;

@Component
@Scope("prototype")
@Mount("dashboard")
public class DashboardPage extends BasePage {

    public DashboardPage() {
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
