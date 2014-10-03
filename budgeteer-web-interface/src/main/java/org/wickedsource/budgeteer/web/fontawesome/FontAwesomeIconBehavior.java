package org.wickedsource.budgeteer.web.fontawesome;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

/**
 * Behavior that can be added to &lt;i&gt;-Tags to create a FontAwesome-Icon.
 */
public class FontAwesomeIconBehavior extends Behavior {

    private FontAwesomeIconType iconType;

    public FontAwesomeIconBehavior(FontAwesomeIconType iconType) {
        this.iconType = iconType;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("class", iconType.getCssClass());
    }
}
