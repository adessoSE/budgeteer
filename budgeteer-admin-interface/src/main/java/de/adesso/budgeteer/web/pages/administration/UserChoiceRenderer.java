package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.service.user.User;

public class UserChoiceRenderer extends AbstractChoiceRenderer<User> {

    @Override
    public Object getDisplayValue(User object) {
        return object.getName();
    }
}
