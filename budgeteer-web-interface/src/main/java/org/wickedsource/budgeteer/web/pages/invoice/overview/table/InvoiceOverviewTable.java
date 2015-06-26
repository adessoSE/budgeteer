package org.wickedsource.budgeteer.web.pages.invoice.overview.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.pages.invoice.edit.EditInvoicePage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class InvoiceOverviewTable extends Panel{
    //PageParameter-Id of the OverviewPage
    private long parentId;

    public InvoiceOverviewTable(String id, InvoiceOverviewTableModel invoiceOverviewTableModel, long parentId) {
        super(id);
        this.parentId = parentId;
        addComponents(invoiceOverviewTableModel);
    }

    private void addComponents(final InvoiceOverviewTableModel data) {
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        table.add(new ListView<String>("headerRow",  model(from(data).getHeadline()) ) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("headerItem", item.getModelObject()));
            }
        });
        table.add(new ListView<InvoiceBaseData>("invoiceRows", model(from(data).getInvoices())) {
            @Override
            protected void populateItem(ListItem<InvoiceBaseData> item) {
                BookmarkablePageLink<EditInvoicePage> link = new BookmarkablePageLink<EditInvoicePage>("editInvoice", EditInvoicePage.class, EditInvoicePage.createEditInvoiceParameters(item.getModelObject().getInvoiceId(), parentId));
                link.add(new Label("invoiceName", model(from(item.getModelObject()).getInvoiceName())));
                item.add(new Label("contractName", model(from(item.getModelObject()).getContractName())));
                item.add(link);
                item.add(new Label("internalNumber", model(from(item.getModelObject()).getInternalNumber())));
                item.add(new Label("year", model(from(item.getModelObject()).getYear())));
                item.add(new Label("month", model(from(item.getModelObject()).getMonth())));
                item.add(new Label("sum", model(from(item.getModelObject()).getSum())));
                CheckBox paid = new CheckBox("paid", model(from(item.getModelObject()).isPaid()));
                paid.setEnabled(false);
                item.add(paid);
                item.add(new ListView<DynamicAttributeField>("invoiceRow", model(from(item.getModelObject()).getDynamicInvoiceFields())) {
                    @Override
                    protected void populateItem(ListItem<DynamicAttributeField> item) {
                        item.add(new Label("invoiceRowText", item.getModelObject().getValue()));
                    }
                });
            }
        });
        table.add(new ListView<String>("footerRow", model(from(data).getFooter()) ) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("footerItem", item.getModelObject()));
            }
        });
        add(table);
    }
}
