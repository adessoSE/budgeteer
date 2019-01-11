package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateModel;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetNameModel;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.EditFixedDailyRatesPage;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.table.FixedDailyRatesTable;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount({"budgets/details/fixedDailyRates/${id}", "budgets/details/fixedDailyRates"})
public class FixedDailyRatesPage extends BasePage {
    @SpringBean
    private FixedDailyRateService fixedDailyRateService;
    private FixedDailyRateModel model;

    public FixedDailyRatesPage(PageParameters parameters) {
        super(parameters);
        model = new FixedDailyRateModel(getPageParameters().get("id").toLong(), fixedDailyRateService);
        FixedDailyRatesTable table = new FixedDailyRatesTable("ratesTable", model, parameters);
        add(table);
        add(createManualRecordLink("addRateLink"));
    }

    private Link createManualRecordLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                WebPage page = new EditFixedDailyRatesPage(FixedDailyRatesPage.class, getPageParameters());
                setResponsePage(page);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(BudgetDetailsPage.class, getPageParameters(),
                new BudgetNameModel(getParameterId())));
        model.addBreadcrumb(new Breadcrumb(FixedDailyRatesPage.class, getPageParameters(), "Fixed Daily Rates"));
        return model;
    }
}