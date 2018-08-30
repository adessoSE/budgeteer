package org.wickedsource.budgeteer.service.user;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class UserMapper extends AbstractMapper<UserEntity, User>{

    @Override
    public User map(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setRoles(entity.getRoles());
        user.setGlobalRole(entity.getGlobalRole());
        return user;
    }
}
