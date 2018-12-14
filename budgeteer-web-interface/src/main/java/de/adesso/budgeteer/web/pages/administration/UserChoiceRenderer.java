package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.service.user.User;
import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class UserChoiceRenderer extends AbstractChoiceRenderer<User> {

    @Override
    public Object getDisplayValue(User object) {
        return object.getName();
    }
}
