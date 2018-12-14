package de.adesso.budgeteer.web.components.fontawesome;

import de.adesso.budgeteer.web.InvalidComponentTagException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class FontAwesomeIcon extends WebMarkupContainer {

    public FontAwesomeIcon(String id, IModel<FontAwesomeIconType> model) {
        super(id, model);
    }

    public FontAwesomeIcon(String id, FontAwesomeIconType iconType) {
        super(id, Model.of(iconType));
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        if (!"i".equals(tag.getName())) {
            throw new InvalidComponentTagException("<i>");
        }
        tag.put("class", ((FontAwesomeIconType) getDefaultModelObject()).getCssClass());
    }
}
