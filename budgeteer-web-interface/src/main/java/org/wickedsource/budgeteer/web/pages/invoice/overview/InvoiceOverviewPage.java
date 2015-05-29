package org.wickedsource.budgeteer.web.pages.invoice.overview;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.invoice.edit.EditInvoicePage;
import org.wickedsource.budgeteer.web.pages.invoice.overview.table.InvoiceOverviewTable;


@Mount({"invoices/${id}", "invoices/"})
public class InvoiceOverviewPage extends BasePage {

    @SpringBean
    private InvoiceService invoiceService;

    public InvoiceOverviewPage(PageParameters parameters) {
        super(parameters);
        InvoiceOverviewTable table;
        if(getParameterId() == 0l){
             table = new InvoiceOverviewTable("invoiceTable", invoiceService.getInvoiceOverviewByProject(BudgeteerSession.get().getProjectId()), getParameterId());
        } else {
             table = new InvoiceOverviewTable("invoiceTable", invoiceService.getInvoiceOverviewByContract(getParameterId()), getParameterId());
        }
        add(table);
        add(new Link("createInvoiceLink") {
            @Override
            public void onClick() {
                WebPage page = new EditInvoicePage(EditInvoicePage.createNewInvoiceParameters(getParameterId()), InvoiceOverviewPage.class, new PageParameters());
                setResponsePage(page);
            }

            @Override
            public boolean isVisible() {
                return getParameterId() != 0l;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, InvoiceOverviewPage.class);
    }
}