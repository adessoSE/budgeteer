package org.wickedsource.budgeteer.web.pages.administration;

import org.wickedsource.budgeteer.service.user.UserModel;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class UserChoiceRenderer extends AbstractChoiceRenderer<UserModel> {

  @Override
  public Object getDisplayValue(UserModel object) {
    return object.getName();
  }
}
