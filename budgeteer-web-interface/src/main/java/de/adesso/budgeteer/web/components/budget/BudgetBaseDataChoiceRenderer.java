package de.adesso.budgeteer.web.components.budget;

import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class BudgetBaseDataChoiceRenderer extends AbstractChoiceRenderer<BudgetBaseData> {

    @Override
    public Object getDisplayValue(BudgetBaseData object) {
        return object.getName();
    }
}
