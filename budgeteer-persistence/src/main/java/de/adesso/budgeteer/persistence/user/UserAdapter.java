package de.adesso.budgeteer.persistence.user;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;
import de.adesso.budgeteer.core.security.port.out.GetAuthenticationUserPort;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.*;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter
    implements GetUsersInProjectPort,
        GetUsersNotInProjectPort,
        UserWithNameExistsPort,
        PasswordMatchesPort,
        UpdateUserPort,
        GetUserByNameAndPasswordPort,
        CreateUserPort,
        GetAuthenticationUserPort,
        GetUserByIdPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public List<User> getUsersInProject(long projectId) {
    return userMapper.mapToDomain(userRepository.findInProject(projectId));
  }

  @Override
  public List<User> getUsersNotInProject(long projectId) {
    return userMapper.mapToDomain(userRepository.findNotInProject(projectId));
  }

  @Override
  public boolean passwordMatches(long userId, String password) {
    return userRepository.passwordMatches(userId, password);
  }

  @Override
  @Transactional
  public void updateUser(long userId, String name, String password) {
    var userEntity = userRepository.findById(userId).orElseThrow();
    userEntity.setName(name);
    if (password != null) {
      userEntity.setPassword(password);
    }
    userRepository.save(userEntity);
  }

  @Override
  public boolean userWithNameExists(String name) {
    return userRepository.existsByName(name);
  }

  @Override
  public Optional<User> getUserByNameAndPassword(String name, String password) {
    return Optional.ofNullable(userRepository.findByNameAndPassword(name, password))
        .map(userMapper::mapToDomain);
  }

  @Override
  public long createUser(String username, String password) {
    var userEntity = new UserEntity();
    userEntity.setName(username);
    userEntity.setPassword(password);
    return userRepository.save(userEntity).getId();
  }

  @Override
  public Optional<AuthenticationUser> getAuthenticationUser(String username) {
    return Optional.ofNullable(userRepository.findByName(username))
        .map(userMapper::mapToAuthenticationUser);
  }

  @Override
  public Optional<User> getUserById(long id) {
    return userRepository.findById(id).map(userMapper::mapToDomain);
  }
}
