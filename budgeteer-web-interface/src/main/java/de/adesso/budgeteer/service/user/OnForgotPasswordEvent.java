package de.adesso.budgeteer.service.user;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.springframework.context.ApplicationEvent;

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
