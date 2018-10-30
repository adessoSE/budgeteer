package org.wickedsource.budgeteer.service.user;

import org.springframework.context.ApplicationEvent;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

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
