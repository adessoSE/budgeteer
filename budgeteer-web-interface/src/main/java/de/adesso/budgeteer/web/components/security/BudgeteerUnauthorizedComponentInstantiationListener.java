package de.adesso.budgeteer.web.components.security;

import de.adesso.budgeteer.web.pages.user.login.LoginPage;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;

public class BudgeteerUnauthorizedComponentInstantiationListener implements IUnauthorizedComponentInstantiationListener {

    @Override
    public void onUnauthorizedInstantiation(Component component) {
        component.setResponsePage(LoginPage.class);
    }
}
