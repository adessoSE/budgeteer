package org.wickedsource.budgeteer.service.user;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

@Component
public class UserMapper {

    public User toDTO(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        return user;
    }

}
