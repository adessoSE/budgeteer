package org.wickedsource.budgeteer.web.component.choicerenderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

public class BudgetBaseDataChoiceRenderer implements IChoiceRenderer<BudgetBaseData> {

    @Override
    public Object getDisplayValue(BudgetBaseData object) {
        return object.getName();
    }

    @Override
    public String getIdValue(BudgetBaseData object, int index) {
        return String.valueOf(object.getId());
    }
}
