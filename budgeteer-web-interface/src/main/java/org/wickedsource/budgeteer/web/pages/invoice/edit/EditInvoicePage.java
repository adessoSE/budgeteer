package org.wickedsource.budgeteer.web.pages.invoice.edit;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.invoice.edit.form.EditInvoiceForm;
import org.wickedsource.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;

@Mount({"invoices/edit/${invoiceId}/${id}", "invoices/edit/${contractId}"})
public class EditInvoicePage extends DialogPageWithBacklink {

	@SpringBean private InvoiceService service;

	public EditInvoicePage(PageParameters parameters) {
		this(parameters, InvoiceOverviewPage.class, parameters);
	}

	public EditInvoicePage(
			PageParameters parameters,
			Class<? extends WebPage> backlinkPage,
			PageParameters backlinkParameters) {
		super(parameters, backlinkPage, backlinkParameters);
		InvoiceBaseData invoiceBaseData;
		if (getInvoiceId() == 0l) {
			invoiceBaseData = service.getEmptyInvoiceModel(getContractId());
		} else {
			invoiceBaseData = service.getInvoiceById(getInvoiceId());
		}
		EditInvoiceForm form = new EditInvoiceForm("form", model(from(invoiceBaseData)));
		addComponents(form);
	}

	private void addComponents(Form<InvoiceBaseData> form) {
		add(createBacklink("cancelButton1"));
		form.add(createBacklink("cancelButton2"));
		add(form);
	}

	private long getInvoiceId() {
		return getIdByName("invoiceId");
	}

	private long getContractId() {
		return getIdByName("contractId");
	}

	private long getIdByName(String name) {
		StringValue value = getPageParameters().get(name);
		if (value == null || value.isEmpty() || value.isNull()) {
			return 0l;
		} else {
			return value.toLong();
		}
	}

	public static PageParameters createEditInvoiceParameters(long invoiceId) {
		PageParameters parameters = new PageParameters();
		parameters.add("invoiceId", invoiceId);
		return parameters;
	}

	public static PageParameters createNewInvoiceParameters(long contractId) {
		PageParameters parameters = new PageParameters();
		parameters.add("contractId", contractId);
		return parameters;
	}
}
