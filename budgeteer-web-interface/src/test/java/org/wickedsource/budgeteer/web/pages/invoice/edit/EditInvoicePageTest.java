package org.wickedsource.budgeteer.web.pages.invoice.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;
import org.wickedsource.budgeteer.web.pages.invoice.overview.table.InvoiceOverviewTableModel;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditInvoicePageTest extends AbstractWebTestTemplate {

    @Autowired
    private InvoiceService invoiceServiceMock;

    @Override
    protected void setupTest() {
        when(invoiceServiceMock.getInvoiceById(anyLong())).thenReturn(new InvoiceBaseData());
        when(invoiceServiceMock.getInvoiceOverviewByProject(anyLong())).thenReturn(new InvoiceOverviewTableModel());
    }

    @Test
    void testRedirectOnId0() {
        WicketTester tester = getTester();
        tester.startPage(EditInvoicePage.class, new PageParameters());
        tester.assertRenderedPage(InvoiceOverviewPage.class);
    }
}
