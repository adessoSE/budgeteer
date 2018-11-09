package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordModel;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.BudgetNameModel;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.AddManualRecordPage;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview.table.ManualRecordOverviewTable;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount({"budgets/details/manuals/${id}", "budgets/details/manuals"})
public class ManualRecordOverviewPage extends BasePage {

    @SpringBean
    private RecordService recordService;

    @SpringBean
    private ManualRecordService manualRecordService;

    private ManualRecordModel model;

    public ManualRecordOverviewPage(PageParameters parameters) {
        super(parameters);
        model = new ManualRecordModel(getPageParameters().get("id").toLong(), manualRecordService);
        add(new ManualRecordOverviewTable("recordTable", model));
        add(createManualRecordLink("addRecordLink"));
    }

    private Link createManualRecordLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                WebPage page = new AddManualRecordPage(ManualRecordOverviewPage.class, getPageParameters());
                setResponsePage(page);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(BudgetDetailsPage.class, getPageParameters(), new BudgetNameModel(getParameterId())));
        model.addBreadcrumb(new Breadcrumb(ManualRecordOverviewPage.class, getPageParameters(), "Manual Records"));

        return model;
    }
}
