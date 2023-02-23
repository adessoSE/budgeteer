package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component("oldUserMapper")
public class UserMapper extends AbstractMapper<UserEntity, User> {

  @Override
  public User map(UserEntity entity) {
    User user = new User();
    user.setId(entity.getId());
    user.setName(entity.getName());
    return user;
  }

  public User map(de.adesso.budgeteer.core.user.domain.User domainUser) {
    var user = new User();
    user.setId(domainUser.getId());
    user.setName(domainUser.getName());
    return user;
  }
}
