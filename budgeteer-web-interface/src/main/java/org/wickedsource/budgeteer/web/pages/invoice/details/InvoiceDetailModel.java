package org.wickedsource.budgeteer.web.pages.invoice.details;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;

public class InvoiceDetailModel extends LoadableDetachableModel<InvoiceBaseData>{

    @SpringBean
    private InvoiceService service;

    private long invoiceId;

    public InvoiceDetailModel(long invoiceId) {
        Injector.get().inject(this);
        this.invoiceId = invoiceId;
    }

    @Override
    protected InvoiceBaseData load() {
        return service.getInvoiceById(invoiceId);
    }

}
