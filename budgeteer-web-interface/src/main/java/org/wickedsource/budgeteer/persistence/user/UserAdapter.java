package org.wickedsource.budgeteer.persistence.user;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;
import de.adesso.budgeteer.core.security.port.out.GetAuthenticationUserPort;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.out.*;
import java.time.LocalDateTime;
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
        GetUserWithEmailPort,
        UserWithEmailExistsPort,
        UserWithNameExistsPort,
        PasswordMatchesPort,
        UpdateUserPort,
        GetUserByNameOrEmailAndPasswordPort,
        CreateUserPort,
        CreateForgottenTokenPort,
        GetUserByEmailPort,
        EmailVerifiedPort,
        ChangeForgottenPasswordPort,
        GetForgottenPasswordTokenExpirationDatePort,
        ForgottenPasswordTokenExistsPort,
        CreateVerificationTokenPort,
        DeleteVerificationTokenByTokenPort,
        DeleteVerificationTokenByUserIdPort,
        VerificationTokenExistsPort,
        GetVerificationTokenExpirationDatePort,
        VerifyEmailPort,
        GetAuthenticationUserPort,
        GetUserByNamePort {

  private final VerificationTokenRepository verificationTokenRepository;
  private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
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
  public UserWithEmail getUserWithEmail(long userId) {
    return userMapper.mapToUserWithEmail(userRepository.findById(userId).orElseThrow());
  }

  @Override
  public boolean passwordMatches(long userId, String password) {
    return userRepository.passwordMatches(userId, password);
  }

  @Override
  @Transactional
  public void updateUser(long userId, String name, String email, String password) {
    var userEntity = userRepository.findById(userId).orElseThrow();
    userEntity.setName(name);
    userEntity.setMail(email);
    if (password != null) {
      userEntity.setPassword(password);
    }
    userRepository.save(userEntity);
  }

  @Override
  public boolean userWithEmailExists(String email) {
    return userRepository.existsByMail(email);
  }

  @Override
  public boolean userWithNameExists(String name) {
    return userRepository.existsByName(name);
  }

  @Override
  public Optional<User> getUserByNameOrEmailAndPassword(String nameOrEmail, String password) {
    return Optional.ofNullable(userRepository.findByNameOrMailAndPassword(nameOrEmail, password))
        .map(userMapper::mapToDomain);
  }

  @Override
  public long createUser(String username, String email, String password) {
    var userEntity = new UserEntity();
    userEntity.setName(username);
    userEntity.setMail(email);
    userEntity.setPassword(password);
    return userRepository.save(userEntity).getId();
  }

  @Override
  public void createForgottenPasswordToken(
      long userId, String token, LocalDateTime expirationDate) {
    var tokenEntity = new ForgotPasswordTokenEntity();
    tokenEntity.setToken(token);
    tokenEntity.setUserEntity(userRepository.findById(userId).orElseThrow());
    tokenEntity.setExpiryDate(expirationDate);
    forgotPasswordTokenRepository.save(tokenEntity);
  }

  @Override
  public Optional<User> getUserByEmail(String mail) {
    return Optional.ofNullable(userRepository.findByMail(mail)).map(userMapper::mapToDomain);
  }

  @Override
  public boolean emailVerified(String email) {
    return userRepository.emailVerified(email);
  }

  @Override
  @Transactional
  public void changeForgottenPassword(String token, String password) {
    userRepository.changePasswordWithForgottenPasswordToken(token, password);
    forgotPasswordTokenRepository.deleteByToken(token);
  }

  @Override
  public boolean forgottenPasswordTokenExists(String token) {
    return forgotPasswordTokenRepository.existsByToken(token);
  }

  @Override
  public LocalDateTime getForgottenPasswordTokenExpirationDate(String token) {
    return forgotPasswordTokenRepository.findByToken(token).getExpiryDate();
  }

  @Override
  @Transactional
  public void createVerificationToken(long userId, String token, LocalDateTime expirationDate) {
    var verificationTokenEntity = new VerificationTokenEntity();
    deleteVerificationTokenByToken(token);
    verificationTokenEntity.setToken(token);
    verificationTokenEntity.setExpiryDate(expirationDate);
    verificationTokenEntity.setUserEntity(userRepository.findById(userId).orElseThrow());
    verificationTokenRepository.save(verificationTokenEntity);
  }

  @Override
  @Transactional
  public void deleteVerificationTokenByToken(String token) {
    verificationTokenRepository.deleteByToken(token);
  }

  @Override
  @Transactional
  public void deleteVerificationTokenByUserId(long userId) {
    verificationTokenRepository.deleteByUserId(userId);
  }

  @Override
  public LocalDateTime getVerificationTokenExpirationDate(String token) {
    return verificationTokenRepository.findByToken(token).getExpiryDate();
  }

  @Override
  public boolean verificationTokenExists(String token) {
    return verificationTokenRepository.existsByToken(token);
  }

  @Override
  @Transactional
  public void verifyEmail(String token) {
    userRepository.verifyEmail(token);
    verificationTokenRepository.deleteByToken(token);
  }

  @Override
  public Optional<AuthenticationUser> getAuthenticationUser(String username) {
    return Optional.ofNullable(userRepository.findByName(username))
        .map(userMapper::mapToAuthenticationUser);
  }

  @Override
  public Optional<User> getUserByName(String name) {
    return Optional.ofNullable(userRepository.findByName(name)).map(userMapper::mapToDomain);
  }
}
