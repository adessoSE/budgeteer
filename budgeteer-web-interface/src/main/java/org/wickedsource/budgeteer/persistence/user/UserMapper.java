package org.wickedsource.budgeteer.persistence.user;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User mapToDomain(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getName());
    }

    public UserWithEmail mapToUserWithEmail(UserEntity userEntity) {
        return new UserWithEmail(userEntity.getId(), userEntity.getName(), userEntity.getMail());
    }

    public List<User> mapToDomain(List<UserEntity> userEntities) {
        return userEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
    }
}
