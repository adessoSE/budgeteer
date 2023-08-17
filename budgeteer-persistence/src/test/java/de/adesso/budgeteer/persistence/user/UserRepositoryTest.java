package de.adesso.budgeteer.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import de.adesso.budgeteer.persistence.DataJpaTestBase;
import de.adesso.budgeteer.persistence.project.ProjectEntity;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  private static final Consumer<UserEntity> DEFAULT_USER =
      userEntity -> {
        userEntity.setName("name");
        userEntity.setPassword("password");
      };

  private static final Consumer<UserEntity> ALTERNATIVE_USER1 =
      userEntity -> {
        userEntity.setName("alt1-name");
        userEntity.setPassword("password");
      };

  private static final Consumer<UserEntity> ALTERNATIVE_USER2 =
      userEntity -> {
        userEntity.setName("alt2-name");
        userEntity.setPassword("password");
      };

  @Test
  void shouldReturnUserByNameAndPasswordIfNameAndPasswordMatch() {
    var expectedUser = persistEntity(new UserEntity(), DEFAULT_USER);

    var returnedUser = userRepository.findByNameAndPassword("name", "password");

    assertThat(returnedUser).isEqualTo(expectedUser);
  }

  @Test
  void shouldReturnNullIfUserWithNameAndPasswordDoesNotExist() {
    persistEntity(new UserEntity(), DEFAULT_USER);

    var returnedUser = userRepository.findByNameAndPassword("other-name", "other-password");

    assertThat(returnedUser).isNull();
  }

  @Test
  void shouldReturnNullIfUserWithNameOrEmailAndPasswordDoesNotExist() {
    persistEntity(new UserEntity(), DEFAULT_USER);

    var returnedUser = userRepository.findByNameAndPassword("name", "other-password");

    assertThat(returnedUser).isNull();
  }

  @Test
  void shouldReturnTrueIfUserWithIdAndPasswordExists() {
    var user = persistEntity(new UserEntity(), DEFAULT_USER);

    var exists = userRepository.passwordMatches(user.getId(), user.getPassword());

    assertThat(exists).isTrue();
  }

  @Test
  void shouldReturnFalseIfUserWithIdAndPasswordDoesNotExist() {
    var user = persistEntity(new UserEntity(), DEFAULT_USER);

    var exists = userRepository.passwordMatches(user.getId(), "other-password");

    assertThat(exists).isFalse();
  }

  @Test
  void shouldReturnAllUsersInProject() {
    var expectedUsers =
        List.of(
            persistEntity(new UserEntity(), DEFAULT_USER),
            persistEntity(new UserEntity(), ALTERNATIVE_USER1));
    persistEntity(new UserEntity(), ALTERNATIVE_USER2);
    var project =
        persistEntity(
            new ProjectEntity(),
            projectEntity -> {
              projectEntity.setName("project");
              projectEntity.setAuthorizedUsers(expectedUsers);
            });

    var returnedUsers = userRepository.findInProject(project.getId());

    assertThat(returnedUsers).isEqualTo(expectedUsers);
  }

  @Test
  void shouldReturnAllUsersNotInProject() {
    var expectedUsers =
        List.of(
            persistEntity(new UserEntity(), DEFAULT_USER),
            persistEntity(new UserEntity(), ALTERNATIVE_USER1));
    var userInProject = persistEntity(new UserEntity(), ALTERNATIVE_USER2);
    var project =
        persistEntity(
            new ProjectEntity(),
            projectEntity -> {
              projectEntity.setName("project");
              projectEntity.setAuthorizedUsers(List.of(userInProject));
            });

    var returnedUsers = userRepository.findNotInProject(project.getId());

    assertThat(returnedUsers).isEqualTo(expectedUsers);
  }
}
