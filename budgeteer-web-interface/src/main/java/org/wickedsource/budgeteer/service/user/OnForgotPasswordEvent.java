package org.wickedsource.budgeteer.service.user;

import org.springframework.context.ApplicationEvent;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

public class OnForgotPasswordEvent extends ApplicationEvent {
    private UserEntity userEntity;

    public OnForgotPasswordEvent(UserEntity userEntity) {
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
