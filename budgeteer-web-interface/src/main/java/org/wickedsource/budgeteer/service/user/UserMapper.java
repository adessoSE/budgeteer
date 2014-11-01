package org.wickedsource.budgeteer.service.user;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public List<User> toDTO(List<UserEntity> entities) {
        List<User> result = new ArrayList<User>();
        for (UserEntity entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public User toDTO(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        return user;
    }

}
