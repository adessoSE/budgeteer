package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
  private UserEntity userEntity;

  public OnRegistrationCompleteEvent(UserEntity userEntity) {
    super(userEntity);

    this.userEntity = userEntity;
  }

  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }
}
