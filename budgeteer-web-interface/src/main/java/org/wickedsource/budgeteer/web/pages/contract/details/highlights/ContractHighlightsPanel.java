package org.wickedsource.budgeteer.web.pages.contract.details.highlights;

import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ContractHighlightsPanel extends Panel {

    public ContractHighlightsPanel(String id, IModel<ContractBaseData> model) {
        super(id, model);
        add(new Label("name", model(from(model.getObject()).getContractName())));
        add(new Label("internalNumber", model(from(model.getObject()).getInternalNumber())));
        add(new Label("year", model(from(model.getObject()).getYear())));
        add(new EnumLabel<ContractEntity.ContractType>("type", model(from(model.getObject()).getType())));
        add(new Label("budget", model(from(model.getObject()).getBudget())));
        add(new ListView<DynamicAttributeField>("additionalInformation", model(from(model.getObject()).getContractAttributes())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("value", model(from(item.getModelObject()).getValue())));
                item.add(new Label("key", model(from(item.getModelObject()).getName())));
            }
        });
    }

}
