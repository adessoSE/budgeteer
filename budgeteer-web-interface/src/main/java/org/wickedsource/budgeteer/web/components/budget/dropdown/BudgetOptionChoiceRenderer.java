package org.wickedsource.budgeteer.web.components.budget.dropdown;

import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public class BudgetOptionChoiceRenderer implements IChoiceRenderer<BudgetOption> {
	@Override
	public Object getDisplayValue(BudgetOption object) {
		return object.getName();
	}

	@Override
	public String getIdValue(BudgetOption object, int index) {
		return String.valueOf(index);
	}

	@Override
	public BudgetOption getObject(String id, IModel<? extends List<? extends BudgetOption>> choices) {
		return choices.getObject().get(Integer.parseInt(id));
	}
}
