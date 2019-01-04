package org.wickedsource.budgeteer.web.pages.invoice.overview.table;

import org.apache.wicket.util.tester.WicketTester;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.components.fileUpload.FileUploadModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

public class InvoiceOverviewTableTest extends AbstractWebTestTemplate {

    @Autowired
    private InvoiceService service;

    private Random random = new Random();

    @Test
    void testTableRender() {
        WicketTester tester = getTester();
        when(service.getEmptyInvoiceModel(anyLong())).thenReturn(createTestData());
        InvoiceOverviewTableModel model = new InvoiceOverviewTableModel();
        InvoiceOverviewTable table = new InvoiceOverviewTable("table", model, null);
        tester.startComponentInPage(table);
    }

    private InvoiceBaseData createTestData() {
        InvoiceBaseData data = new InvoiceBaseData();
        data.setContractId(1L);
        data.setContractName("contract");
        data.setFileUploadModel(new FileUploadModel());
        data.setInternalNumber("internalNumber");
        data.setInvoiceId(1L);
        data.setInvoiceName("invoice");
        data.setDueDate(new Date());
        data.setPaidDate(new Date());
        Money sum = Money.of(CurrencyUnit.EUR, random.nextInt());
        data.setSum(sum);
        BigDecimal taxRate = new BigDecimal(random.nextInt(100));
        data.setTaxRate(taxRate);
        double taxAmount = sum.getAmount().doubleValue() * taxRate.doubleValue() / 100;
        data.setTaxAmount(Money.of(CurrencyUnit.EUR, taxAmount));
        double sumGross = sum.getAmount().doubleValue() + taxAmount;
        data.setSum_gross(Money.of(CurrencyUnit.EUR, sumGross));
        return data;
    }

    @Override
    protected void setupTest() {
    }
}
