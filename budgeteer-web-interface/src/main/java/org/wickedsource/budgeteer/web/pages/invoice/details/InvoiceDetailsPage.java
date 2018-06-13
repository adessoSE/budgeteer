package org.wickedsource.budgeteer.web.pages.invoice.details;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.confirm.ConfirmationForm;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailsPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.invoice.details.highlights.InvoiceHighlightsPanel;
import org.wickedsource.budgeteer.web.pages.invoice.edit.EditInvoicePage;

@Mount("invoices/details/${id}")
public class InvoiceDetailsPage extends BasePage {

	@SpringBean private InvoiceService invoiceService;

	public InvoiceDetailsPage(PageParameters parameters) {
		super(parameters);
		add(new InvoiceHighlightsPanel("highlightsPanel", new InvoiceDetailModel(getParameterId())));
		add(
				new Link("editLink") {
					@Override
					public void onClick() {
						WebPage page =
								new EditInvoicePage(
										EditInvoicePage.createEditInvoiceParameters(getParameterId()),
										InvoiceDetailsPage.class,
										getPageParameters());
						setResponsePage(page);
					}
				});
		Form deleteForm =
				new ConfirmationForm("deleteForm", this, "confirmation.delete") {
					@Override
					public void onSubmit() {
						invoiceService.deleteInvoice(getParameterId());
						setResponsePage(DashboardPage.class);
					}
				};
		deleteForm.add(new SubmitLink("deleteLink"));
		add(deleteForm);
	}

	@Override
	protected BreadcrumbsModel getBreadcrumbsModel() {
		BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, ContractDetailsPage.class);
		model.addBreadcrumb(InvoiceDetailsPage.class, getPageParameters());
		return model;
	}
}
