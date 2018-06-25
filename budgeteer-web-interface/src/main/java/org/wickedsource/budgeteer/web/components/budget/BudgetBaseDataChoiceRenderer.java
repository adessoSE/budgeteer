package org.wickedsource.budgeteer.web.components.budget;

import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class BudgetBaseDataChoiceRenderer extends AbstractChoiceRenderer<BudgetBaseData> {

	@Override
	public Object getDisplayValue(BudgetBaseData object) {
		return object.getName();
	}
}
