package org.wickedsource.budgeteer.web.pages.dashboard;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class UsernameModel extends AbstractReadOnlyModel<String> {

	@Override
	public String getObject() {
		return BudgeteerSession.get().getLoggedInUser().getName();
	}
}
