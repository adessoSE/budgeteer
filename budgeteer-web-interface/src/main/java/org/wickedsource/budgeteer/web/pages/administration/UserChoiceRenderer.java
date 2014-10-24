package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.service.user.User;

public class UserChoiceRenderer implements IChoiceRenderer<User> {

    @Override
    public Object getDisplayValue(User object) {
        return object.getName();
    }

    @Override
    public String getIdValue(User object, int index) {
        return String.valueOf(index);
    }

}
