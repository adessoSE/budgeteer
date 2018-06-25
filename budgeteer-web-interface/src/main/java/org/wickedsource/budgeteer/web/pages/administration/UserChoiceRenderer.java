package org.wickedsource.budgeteer.web.pages.administration;

import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class UserChoiceRenderer extends AbstractChoiceRenderer<User> {

	@Override
	public Object getDisplayValue(User object) {
		return object.getName();
	}
}
