package org.wickedsource.budgeteer.web.pages.invoice.edit;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;
import org.wickedsource.budgeteer.web.pages.invoice.overview.table.InvoiceOverviewTableModel;

class EditInvoicePageTest extends AbstractWebTestTemplate {

  @MockBean private InvoiceService invoiceServiceMock;

  @Override
  protected void setupTest() {
    when(invoiceServiceMock.getInvoiceById(anyLong())).thenReturn(new InvoiceBaseData());
    when(invoiceServiceMock.getInvoiceOverviewByProject(anyLong()))
        .thenReturn(new InvoiceOverviewTableModel());
  }

  @Test
  void testRedirectOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditInvoicePage.class, new PageParameters());
    tester.assertRenderedPage(InvoiceOverviewPage.class);
  }
}
