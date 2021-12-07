package org.wickedsource.budgeteer.persistence.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.DataJpaTestBase;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends DataJpaTestBase {

    @Autowired private UserRepository userRepository;

    private static final Consumer<UserEntity> DEFAULT_USER = userEntity -> {
        userEntity.setName("name");
        userEntity.setMail("e@mail");
        userEntity.setPassword("password");
    };

    private static final Consumer<UserEntity> ALTERNATIVE_USER1 = userEntity -> {
        userEntity.setName("alt1-name");
        userEntity.setMail("alt1@mail");
        userEntity.setPassword("password");
    };

    private static final Consumer<UserEntity> ALTERNATIVE_USER2 = userEntity -> {
        userEntity.setName("alt2-name");
        userEntity.setMail("alt2@mail");
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
    void shouldReturnUserByNameAndPasswordIfNameOrEmailAndPasswordMatchesNameAndPassword() {
        var expectedUser = persistEntity(new UserEntity(), DEFAULT_USER);

        var returnedUser = userRepository.findByNameOrMailAndPassword("name", "password");

        assertThat(returnedUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldReturnUserByEmailAndPasswordIfNameOrEmailAndPasswordMatchesEmailAndPassword() {
        var expectedUser = persistEntity(new UserEntity(), DEFAULT_USER);

        var returnedUser = userRepository.findByNameOrMailAndPassword("e@mail", "password");

        assertThat(returnedUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldReturnNullIfUserWithNameOrEmailAndPasswordDoesNotExist() {
        persistEntity(new UserEntity(), DEFAULT_USER);

        var returnedUser = userRepository.findByNameOrMailAndPassword("ether@mail", "other-password");

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
    void shouldReturnTrueIfEmailIsVerified() {
        var user = persistEntity(new UserEntity(), DEFAULT_USER.andThen(userEntity -> userEntity.setMailVerified(true)));

        var verified = userRepository.emailVerified(user.getMail());

        assertThat(verified).isTrue();
    }

    @Test
    void shouldReturnFalseIfEmailIsNotVerified() {
        var user = persistEntity(new UserEntity(), DEFAULT_USER.andThen(userEntity -> userEntity.setMailVerified(false)));

        var verified = userRepository.emailVerified(user.getMail());

        assertThat(verified).isFalse();
    }

    @Test
    void shouldChangePasswordIfForgottenPasswordTokenExists() {
        var newPassword = "new-password";
        var originalUser = persistEntity(new UserEntity(), DEFAULT_USER);
        var forgottenPasswordToken = persistEntity(new ForgotPasswordTokenEntity(), token -> {
            token.setToken("token");
            token.setExpiryDate(LocalDateTime.now().plusDays(1));
            token.setUserEntity(originalUser);
        });

        userRepository.changePasswordWithForgottenPasswordToken(forgottenPasswordToken.getToken(), newPassword);
        entityManager.clear();

        var updatedUser = entityManager.find(UserEntity.class, originalUser.getId());
        assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void shouldVerifyEmailIfVerificationTokenExists() {
        var originalUser = persistEntity(new UserEntity(), DEFAULT_USER.andThen(user -> user.setMailVerified(false)));
        var verificationToken = persistEntity(new VerificationTokenEntity(), token -> {
            token.setToken("token");
            token.setExpiryDate(LocalDateTime.now().plusDays(1));
            token.setUserEntity(originalUser);
        });

        userRepository.verifyEmail(verificationToken.getToken());
        entityManager.clear();

        var updatedUser = entityManager.find(UserEntity.class, originalUser.getId());
        assertThat(updatedUser.getMailVerified()).isTrue();
    }

    @Test
    void shouldReturnAllUsersInProject() {
        var expectedUsers = List.of(persistEntity(new UserEntity(), DEFAULT_USER), persistEntity(new UserEntity(), ALTERNATIVE_USER1));
        persistEntity(new UserEntity(), ALTERNATIVE_USER2);
        var project = persistEntity(new ProjectEntity(), projectEntity -> {
            projectEntity.setName("project");
            projectEntity.setAuthorizedUsers(expectedUsers);
        });

        var returnedUsers = userRepository.findInProject(project.getId());

        assertThat(returnedUsers).isEqualTo(expectedUsers);
    }

    @Test
    void shouldReturnAllUsersNotInProject() {
        var expectedUsers = List.of(persistEntity(new UserEntity(), DEFAULT_USER), persistEntity(new UserEntity(), ALTERNATIVE_USER1));
        var userInProject = persistEntity(new UserEntity(), ALTERNATIVE_USER2);
        var project = persistEntity(new ProjectEntity(), projectEntity -> {
            projectEntity.setName("project");
            projectEntity.setAuthorizedUsers(List.of(userInProject));
        });

        var returnedUsers = userRepository.findNotInProject(project.getId());

        assertThat(returnedUsers).isEqualTo(expectedUsers);
    }
}
