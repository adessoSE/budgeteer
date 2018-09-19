package org.wickedsource.budgeteer.web.pages.invoice.details.highlights;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

import java.io.IOException;
import java.io.OutputStream;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class InvoiceHighlightsPanel extends GenericPanel<InvoiceBaseData> {

    Label sumGrosslabel;
    Label taxAmountLabel;
    Label taxRateLabel;

    public InvoiceHighlightsPanel(String id, final IModel<InvoiceBaseData> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("name", model(from(getModelObject()).getInvoiceName())));
        add(new Label("contractName", model(from(getModelObject()).getContractName())));
        add(new Label("internalNumber", model(from(getModelObject()).getInternalNumber())));
        add(new Label("year", model(from(getModelObject()).getYear())));
        add(new Label("month", PropertyLoader.getProperty(BasePage.class, "monthRenderer.name." + getModelObject().getMonth())));
        add(new Label("sum", model(from(getModelObject()).getSum())));
        sumGrosslabel = new Label("sum_gross", model(from(getModelObject()).getSum_gross()));
        add(sumGrosslabel);
        taxAmountLabel = new Label("taxAmount", model(from(getModelObject()).getTaxAmount()));
        add(taxAmountLabel);
        taxRateLabel = new Label("taxRate", getModelObject().getTaxRate().doubleValue() + " %");
        add(taxRateLabel);
        setTaxInformationVisible(false);
        add(new Label("paid", (getModelObject().isPaid() ? getString("invoice.paid.yes") : getString("invoice.paid.no"))));


        WebMarkupContainer linkContainer = new WebMarkupContainer("linkContainer") {
            @Override
            public boolean isVisible() {
                return getModelObject().getFileUploadModel().getLink() != null && !getModelObject().getFileUploadModel().getLink().isEmpty();
            }
        };
        linkContainer.add(new ExternalLink("link", Model.of(getModelObject().getFileUploadModel().getLink()), Model.of(getModelObject().getFileUploadModel().getLink())));
        add(linkContainer);

        WebMarkupContainer fileContainer = new WebMarkupContainer("fileContainer") {
            @Override
            public boolean isVisible() {
                return getModelObject().getFileUploadModel().getFileName() != null && !getModelObject().getFileUploadModel().getFileName().isEmpty();
            }
        };
        final byte[] file = getModelObject().getFileUploadModel().getFile();
        final String fileName = getModelObject().getFileUploadModel().getFileName();
        Link<Void> fileDownloadLink = new Link<Void>("file") {

            @Override
            public void onClick() {
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(file);
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        fileDownloadLink.setBody(Model.of(fileName));
        fileContainer.add(fileDownloadLink);
        add(fileContainer);

        add(new ListView<DynamicAttributeField>("additionalInformation", model(from(getModelObject()).getDynamicInvoiceFields())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("value", model(from(item.getModelObject()).getValue())));
                item.add(new Label("key", model(from(item.getModelObject()).getName())));
            }
        });
    }

    public void setTaxInformationVisible(boolean visible) {
        sumGrosslabel.setVisible(visible);
        taxAmountLabel.setVisible(visible);
        taxRateLabel.setVisible(visible);
    }
}
