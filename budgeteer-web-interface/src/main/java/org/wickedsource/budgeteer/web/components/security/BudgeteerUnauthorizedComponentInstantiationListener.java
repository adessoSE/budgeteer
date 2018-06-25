package org.wickedsource.budgeteer.web.components.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

public class BudgeteerUnauthorizedComponentInstantiationListener implements IUnauthorizedComponentInstantiationListener {

	@Override
	public void onUnauthorizedInstantiation(Component component) {
		component.setResponsePage(LoginPage.class);
	}
}
