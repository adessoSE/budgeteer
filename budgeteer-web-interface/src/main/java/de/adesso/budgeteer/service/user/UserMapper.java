package de.adesso.budgeteer.service.user;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.springframework.stereotype.Component;
import de.adesso.budgeteer.service.AbstractMapper;

@Component
public class UserMapper extends AbstractMapper<UserEntity, User>{

    @Override
    public User map(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setLastLogin(entity.getLastLogin());
        return user;
    }
}
