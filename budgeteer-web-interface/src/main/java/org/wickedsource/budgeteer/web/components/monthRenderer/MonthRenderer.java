package org.wickedsource.budgeteer.web.components.monthRenderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

public class MonthRenderer extends AbstractChoiceRenderer<Integer> {
    @Override
    public Object getDisplayValue(Integer object) {
        return PropertyLoader.getProperty(BasePage.class, "monthRenderer.name." + object);
    }

    @Override
    public String getIdValue(Integer object, int index) {
        return "" + index;
    }
}
