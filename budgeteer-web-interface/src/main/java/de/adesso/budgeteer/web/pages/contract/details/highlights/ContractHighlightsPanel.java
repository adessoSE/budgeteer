package de.adesso.budgeteer.web.pages.contract.details.highlights;

import de.adesso.budgeteer.service.contract.ContractBaseData;
import de.adesso.budgeteer.service.contract.DynamicAttributeField;
import de.adesso.budgeteer.web.components.MarqueeLabel;
import de.adesso.budgeteer.web.components.datelabel.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
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

import java.io.IOException;
import java.io.OutputStream;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ContractHighlightsPanel extends Panel {

    public ContractHighlightsPanel(String id, final IModel<ContractBaseData> model) {
        super(id, model);
        add(new MarqueeLabel("name", model(from(model.getObject()).getContractName())));
        add(new Label("internalNumber", model(from(model.getObject()).getInternalNumber())));
        add(new DateLabel("startDate", model(from(model.getObject()).getStartDate())));
        add(new EnumLabel<>("type", model(from(model.getObject()).getType())));
        add(new Label("budget", model(from(model.getObject()).getBudget())));

        WebMarkupContainer linkContainer = new WebMarkupContainer("linkContainer"){
            @Override
            public boolean isVisible() {
                return model.getObject().getFileModel().getLink() != null && !model.getObject().getFileModel().getLink().isEmpty();
            }
        };
        linkContainer.add(new ExternalLink("link", Model.of(model.getObject().getFileModel().getLink()), Model.of(model.getObject().getFileModel().getLink())));
        add(linkContainer);

        WebMarkupContainer fileContainer = new WebMarkupContainer("fileContainer"){
            @Override
            public boolean isVisible() {
                return model.getObject().getFileModel().getFileName() != null && !model.getObject().getFileModel().getFileName().isEmpty();
            }
        };
        final byte[] file = model.getObject().getFileModel().getFile();
        final String fileName = model.getObject().getFileModel().getFileName();
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

        add(new ListView<DynamicAttributeField>("additionalInformation", model(from(model.getObject()).getContractAttributes())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("value", model(from(item.getModelObject()).getValue())));
                item.add(new Label("key", model(from(item.getModelObject()).getName())));
            }
        });
    }

}
