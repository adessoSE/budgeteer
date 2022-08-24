package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.core.user.domain.User;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class UserModelMapper extends AbstractMapper<User, UserModel> {

  @Override
  public UserModel map(User user) {
    var model = new UserModel();
    model.setId(user.getId());
    model.setName(user.getName());
    return model;
  }
}
