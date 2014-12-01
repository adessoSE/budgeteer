package org.wickedsource.budgeteer.web.pages.budgets.details;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.components.confirm.ConfirmationForm;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetBasePage;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetNameModel;
import org.wickedsource.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChart;
import org.wickedsource.budgeteer.web.pages.budgets.details.chart.PeopleDistributionChartModel;
import org.wickedsource.budgeteer.web.pages.budgets.details.highlights.BudgetHighlightsModel;
import org.wickedsource.budgeteer.web.pages.budgets.details.highlights.BudgetHighlightsPanel;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.budgets.hours.BudgetHoursPage;
import org.wickedsource.budgeteer.web.pages.budgets.monthreport.single.SingleBudgetMonthReportPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.budgets.weekreport.single.SingleBudgetWeekReportPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("budgets/details/${id}")
public class BudgetDetailsPage extends BudgetBasePage {

    @SpringBean
    private BudgetService budgetService;

    public BudgetDetailsPage(PageParameters parameters) {
        super(parameters);
        add(new BudgetHighlightsPanel("highlightsPanel", new BudgetHighlightsModel(getBudgetId())));
        add(new PeopleDistributionChart("distributionChart", new PeopleDistributionChartModel(getBudgetId()), new BudgeteerChartTheme()));
        add(new BookmarkablePageLink<SingleBudgetWeekReportPage>("weekReportLink1", SingleBudgetWeekReportPage.class, SingleBudgetWeekReportPage.createParameters(getBudgetId())));
        add(new BookmarkablePageLink<SingleBudgetWeekReportPage>("weekReportLink2", SingleBudgetWeekReportPage.class, SingleBudgetWeekReportPage.createParameters(getBudgetId())));
        add(new BookmarkablePageLink<SingleBudgetMonthReportPage>("monthReportLink1", SingleBudgetMonthReportPage.class, SingleBudgetMonthReportPage.createParameters(getBudgetId())));
        add(new BookmarkablePageLink<SingleBudgetMonthReportPage>("monthReportLink2", SingleBudgetMonthReportPage.class, SingleBudgetMonthReportPage.createParameters(getBudgetId())));
        add(new BookmarkablePageLink<BudgetHoursPage>("hoursLink1", BudgetHoursPage.class, BudgetHoursPage.createParameters(getBudgetId())));
        add(new BookmarkablePageLink<BudgetHoursPage>("hoursLink2", BudgetHoursPage.class, BudgetHoursPage.createParameters(getBudgetId())));
        add(createEditLink("editLink1"));
        add(createEditLink("editLink2"));

        Form deleteForm = new ConfirmationForm("deleteForm", this, "confirmation.delete") {
            @Override
            public void onSubmit() {
                budgetService.deleteBudget(getBudgetId());
                setResponsePage(BudgetsOverviewPage.class);
            }
        };
        deleteForm.add(createSubmitLink("deleteLink1"));
        deleteForm.add(createSubmitLink("deleteLink2"));
        add(deleteForm);
    }

    private Link createEditLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                WebPage page = new EditBudgetPage(EditBudgetPage.createParameters(getBudgetId()), BudgetDetailsPage.class, getPageParameters());
                setResponsePage(page);
            }
        };
    }

    private SubmitLink createSubmitLink(String id) {
        return new SubmitLink(id);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(BudgetDetailsPage.class, getPageParameters(), new BudgetNameModel(getBudgetId())));
        return model;
    }

}
