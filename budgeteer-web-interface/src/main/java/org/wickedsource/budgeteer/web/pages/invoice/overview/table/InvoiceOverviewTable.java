package org.wickedsource.budgeteer.web.pages.invoice.overview.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.contract.details.ContractDetailsPage;
import org.wickedsource.budgeteer.web.pages.invoice.details.InvoiceDetailsPage;
import org.wickedsource.budgeteer.web.pages.invoice.edit.EditInvoicePage;
import org.wickedsource.budgeteer.web.pages.invoice.overview.InvoiceOverviewPage;

import java.math.BigDecimal;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class InvoiceOverviewTable extends Panel {
    private final BreadcrumbsModel breadcrumbsModel;

    public InvoiceOverviewTable(String id, InvoiceOverviewTableModel invoiceOverviewTableModel, BreadcrumbsModel breadcrumbsModel) {
        super(id);
        addComponents(invoiceOverviewTableModel);
        this.breadcrumbsModel = breadcrumbsModel;
    }

    private void addComponents(final InvoiceOverviewTableModel data) {
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        table.add(new ListView<String>("headerRow", model(from(data).getHeadline())) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("headerItem", item.getModelObject()));
            }
        });
        table.add(new ListView<InvoiceBaseData>("invoiceRows", model(from(data).getInvoices())) {
            @Override
            protected void populateItem(final ListItem<InvoiceBaseData> item) {
                final long invoiceId = item.getModelObject().getInvoiceId();

                ExternalLink contractLink;
                contractLink = new ExternalLink("contractLink", "/contracts/details/" + item.getModelObject().getContractId());

                ExternalLink link = new ExternalLink("showInvoice", "/invoices/details/" + item.getModelObject().getInvoiceId());

                contractLink.setContextRelative(false);
                link.setContextRelative(false);
                link.add(new Label("invoiceName", model(from(item.getModelObject()).getInvoiceName())));

                item.add(link);
                contractLink.add(new Label("contractName", model(from(item.getModelObject()).getContractName())));
                item.add(contractLink);

                item.add(new Label("internalNumber", model(from(item.getModelObject()).getInternalNumber())));
                item.add(new Label("year", model(from(item.getModelObject()).getYear())));
                item.add(new Label("month_number", getMonthNumberAsString(item.getModelObject().getMonth())));
                item.add(new Label("month", PropertyLoader.getProperty(BasePage.class, "monthRenderer.name." + item.getModelObject().getMonth())));
                item.add(new Label("sum", Model.of(MoneyUtil.toDouble(item.getModelObject().getSum(), BudgeteerSession.get().getSelectedBudgetUnit()))));
                item.add(new Label("sum_gross", Model.of(MoneyUtil.toDouble(item.getModelObject().getSum_gross(), BudgeteerSession.get().getSelectedBudgetUnit()))));
                item.add(new Label("taxAmount", Model.of(MoneyUtil.toDouble(item.getModelObject().getTaxAmount(), BudgeteerSession.get().getSelectedBudgetUnit()))));
                item.add(new Label("taxRate", getTaxRateAsString(item.getModelObject().getTaxRate())));

                CheckBox paid = new CheckBox("paid", model(from(item.getModelObject()).isPaid()));
                paid.setEnabled(false);
                item.add(paid);

                item.add(new ListView<DynamicAttributeField>("invoiceRow", model(from(item.getModelObject()).getDynamicInvoiceFields())) {
                    @Override
                    protected void populateItem(ListItem<DynamicAttributeField> item) {
                        item.add(new Label("invoiceRowText", item.getModelObject().getValue()));
                    }
                });

                item.add(new Link("editLink"){
                    @Override
                    public void onClick() {
                        setResponsePage(new EditInvoicePage(EditInvoicePage.createEditInvoiceParameters(invoiceId), this.getWebPage().getClass(), this.getPage().getPageParameters()));
                    }
                });
            };
        });
        table.add(new ListView<String>("footerRow", model(from(data).getFooter())) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("footerItem", item.getModelObject()));
            }
        });
        add(table);
    }

    private String getMonthNumberAsString(int month) {
        String r = "" + month;
        if (r.length() < 2) {
            r = "0" + r;
        }
        return r;
    }

    /***
     * Add a percent sign to the tax rate and show only the needed decimal places
     * @param taxRate tax rate of the invoice's contract
     * @return a string of the tax rate with a percent sign
     */
    private String getTaxRateAsString(BigDecimal taxRate) {
        // convert the taxRate to a double first to show only the needed decimal places
        return taxRate.doubleValue() + " %";
    }
}
