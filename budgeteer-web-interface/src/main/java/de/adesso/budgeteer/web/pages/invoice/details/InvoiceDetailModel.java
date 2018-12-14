package de.adesso.budgeteer.web.pages.invoice.details;

import de.adesso.budgeteer.service.invoice.InvoiceBaseData;
import de.adesso.budgeteer.service.invoice.InvoiceService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
