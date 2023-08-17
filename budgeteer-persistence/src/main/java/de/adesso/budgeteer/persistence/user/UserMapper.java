package de.adesso.budgeteer.persistence.user;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;
import de.adesso.budgeteer.core.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public User mapToDomain(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getName());
  }

  public List<User> mapToDomain(List<UserEntity> userEntities) {
    return userEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
  }

  public AuthenticationUser mapToAuthenticationUser(UserEntity userEntity) {
    return new AuthenticationUser(
        userEntity.getId(), userEntity.getName(), userEntity.getPassword());
  }
}
