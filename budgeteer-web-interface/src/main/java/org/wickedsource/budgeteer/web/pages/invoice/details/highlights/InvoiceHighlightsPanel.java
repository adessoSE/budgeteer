package org.wickedsource.budgeteer.web.pages.invoice.details.highlights;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;

import java.io.IOException;
import java.io.OutputStream;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class InvoiceHighlightsPanel extends Panel {

    public InvoiceHighlightsPanel(String id, final IModel<InvoiceBaseData> model) {
        super(id, model);
        add(new Label("name", model(from(model.getObject()).getInvoiceName())));
        add(new Label("contractName", model(from(model.getObject()).getContractName())));
        add(new Label("internalNumber", model(from(model.getObject()).getInternalNumber())));
        add(new Label("year", model(from(model.getObject()).getYear())));
        add(new Label("month", model(from(model.getObject()).getMonth())));
        add(new MoneyLabel("sum", model(from(model.getObject()).getSum())));
        add(new Label("paid", (model.getObject().isPaid() ? getString("invoice.paid.yes") : getString("invoice.paid.no"))));

        WebMarkupContainer linkContainer = new WebMarkupContainer("linkContainer"){
            @Override
            public boolean isVisible() {
                return model.getObject().getFileUploadModel().getLink() != null && !model.getObject().getFileUploadModel().getLink().isEmpty();
            }
        };
        linkContainer.add(new ExternalLink("link", Model.of(model.getObject().getFileUploadModel().getLink()), Model.of(model.getObject().getFileUploadModel().getLink())));
        add(linkContainer);

        WebMarkupContainer fileContainer = new WebMarkupContainer("fileContainer"){
            @Override
            public boolean isVisible() {
                return model.getObject().getFileUploadModel().getFileName() != null && !model.getObject().getFileUploadModel().getFileName().isEmpty();
            }
        };
        final byte[] file = model.getObject().getFileUploadModel().getFile();
        final String fileName = model.getObject().getFileUploadModel().getFileName();
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

        add(new ListView<DynamicAttributeField>("additionalInformation", model(from(model.getObject()).getDynamicInvoiceFields())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("value", model(from(item.getModelObject()).getValue())));
                item.add(new Label("key", model(from(item.getModelObject()).getName())));
            }
        });
    }

}
