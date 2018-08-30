package de.adesso.budgeteer.web.components.monthRenderer;

import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.PropertyLoader;

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
