package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.AddUserToProjectUseCase;
import de.adesso.budgeteer.core.project.port.in.RemoveUserFromProjectUseCase;
import de.adesso.budgeteer.core.user.*;
import de.adesso.budgeteer.core.user.port.in.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.UnknownEntityException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final GetUsersInProjectUseCase getUsersInProjectUseCase;
  private final GetUsersNotInProjectUseCase getUsersNotInProjectUseCase;
  private final RemoveUserFromProjectUseCase removeUserFromProjectUseCase;
  private final AddUserToProjectUseCase addUserToProjectUseCase;
  private final LoginUseCase loginUseCase;
  private final RegisterUseCase registerUseCase;
  private final GetUserWithEmailUseCase getUserWithEmailUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final VerifyEmailUseCase verifyEmailUseCase;
  private final ResetPasswordUseCase resetPasswordUseCase;
  private final PasswordHasher passwordHasher;
  private final UserMapper mapper;

  /**
   * Returns a list of all users that currently have access to the given project.
   *
   * @param projectId ID of the project whose users to load.
   * @return list of all users with access to the given project.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public List<User> getUsersInProject(long projectId) {
    return getUsersInProjectUseCase.getUsersInProject(projectId).stream()
        .map(mapper::map)
        .collect(Collectors.toList());
  }

  /**
   * Returns a list of all users that currently DO NOT have access to the given project.
   *
   * @param projectId ID of the project
   * @return list of all users that currently DO NOT have access to the given project.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public List<User> getUsersNotInProject(long projectId) {
    return getUsersNotInProjectUseCase.getUsersNotInProject(projectId).stream()
        .map(mapper::map)
        .collect(Collectors.toList());
  }

  /**
   * Removes the the access of the given user to the given project.
   *
   * @param projectId ID of the project for which to remove access
   * @param userId ID of the user whose access to remove.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public void removeUserFromProject(long projectId, long userId) {
    try {
      removeUserFromProjectUseCase.removeUserFromProject(userId, projectId);
    } catch (NotFoundException e) {
      var id = e.getClazz() == Project.class ? projectId : userId;
      throw new UnknownEntityException(e.getClazz(), id);
    }
  }

  /**
   * Adds the given user to the given project so that this user now has access to it.
   *
   * @param projectId ID of the project.
   * @param userId ID of the user.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public void addUserToProject(long projectId, long userId) {
    try {
      addUserToProjectUseCase.addUserToProject(userId, projectId);
    } catch (NotFoundException e) {
      var id = e.getClazz() == Project.class ? projectId : userId;
      throw new UnknownEntityException(e.getClazz(), id);
    }
  }

  /**
   * Checks a users login credentials.
   *
   * @param username username or mail of the user to login
   * @param password plain text password of the user to login
   * @return a User object of the logged in user on successful login.
   * @throws InvalidLoginCredentialsException in case of invalid credentials
   */
  public User login(String username, String password) throws InvalidLoginCredentialsException {
    try {
      return mapper.map(loginUseCase.login(username, passwordHasher.hash(password)));
    } catch (de.adesso.budgeteer.core.user.InvalidLoginCredentialsException e) {
      throw new InvalidLoginCredentialsException();
    }
  }

  /**
   * Registers a new user to the Budgeteer application. If the chosen username is already in-use, an
   * UsernameAlreadyInUseException is thrown. If the chosen mail address is already in-use, a
   * MailAlreadyInUseException is thrown. An event is triggered to signal the application that a new
   * user has been registered.
   *
   * @param username the users name
   * @param mail the users mail address
   * @param password the users password
   */
  public void registerUser(String username, String mail, String password)
      throws UsernameAlreadyInUseException, MailAlreadyInUseException {
    try {
      registerUseCase.register(
          new RegisterUseCase.RegisterCommand(username, mail, passwordHasher.hash(password)));
    } catch (UserException e) {
      if (e.getCauses().contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE)) {
        throw new UsernameAlreadyInUseException();
      }
      if (e.getCauses().contains(UserException.UserErrors.MAIL_ALREADY_IN_USE)) {
        throw new MailAlreadyInUseException();
      }
    }
  }

  /**
   * Checks a password for matching the registered password.
   *
   * @param id the ID of the user
   * @param password password to be checked
   * @return true if the password matches the user, false if not
   */
  public boolean checkPassword(long id, String password) {
    var user = getUserWithEmailUseCase.getUserWithEmail(id);
    try {
      loginUseCase.login(user.getName(), passwordHasher.hash(password));
    } catch (de.adesso.budgeteer.core.user.InvalidLoginCredentialsException e) {
      return false;
    }
    return true;
  }

  /**
   * Triggers an OnForgotPasswordEvent for the given user with the passed mail address.
   *
   * @param mail the users mail address
   * @throws MailNotFoundException
   * @throws MailNotVerifiedException
   */
  public void resetPassword(String mail) throws MailNotFoundException, MailNotVerifiedException {
    try {
      resetPasswordUseCase.resetPassword(mail);
    } catch (de.adesso.budgeteer.core.user.MailNotFoundException e) {
      throw new MailNotFoundException();
    } catch (de.adesso.budgeteer.core.user.MailNotVerifiedException e) {
      throw new MailNotVerifiedException();
    } catch (MailNotEnabledException e) {
      /* Do nothing */
    }
  }

  /**
   * Creates an EditUserData for a user to be able to edit him easy.
   *
   * @param id the ID of the user
   * @return EditUserData for editing the user
   */
  public EditUserData loadUserToEdit(long id) {
    var user =
        Optional.ofNullable(getUserWithEmailUseCase.getUserWithEmail(id))
            .orElseThrow(() -> new UnknownEntityException(User.class, id));
    EditUserData editUserData = new EditUserData();
    editUserData.setId(user.getId());
    editUserData.setName(user.getName());
    editUserData.setMail(user.getEmail());
    return editUserData;
  }

  /**
   * Saves a user with the data passed.
   *
   * <p>If the name already exists, an UsernameAlreadyInUseException is thrown. If the mail address
   * already exists, a MailAlreadyInUseException is thrown.
   *
   * <p>If the user changes his mail address, getMailVerified is set to false, because the user has
   * to verify the new mail address again. If the user changes his password, the new password is
   * hashed and then saved.
   *
   * @param data the new data for the user
   * @param changePassword specifies if the password should also be changed
   * @return true if the mail address is verified, otherwise false
   * @throws UsernameAlreadyInUseException
   * @throws MailAlreadyInUseException
   */
  public boolean saveUser(EditUserData data)
      throws UsernameAlreadyInUseException, MailAlreadyInUseException {
    try {
      updateUserUseCase.updateUser(
          new UpdateUserUseCase.UpdateUserCommand(
              data.getId(),
              data.getName(),
              data.getMail(),
              passwordHasher.hash(data.getPassword()),
              passwordHasher.hash(data.getNewPassword())));
    } catch (de.adesso.budgeteer.core.user.UsernameAlreadyInUseException e) {
      throw new UsernameAlreadyInUseException();
    } catch (de.adesso.budgeteer.core.user.MailAlreadyInUseException e) {
      throw new MailAlreadyInUseException();
    } catch (de.adesso.budgeteer.core.user.InvalidLoginCredentialsException e) {
      /* Do nothing */
    }
    return true;
  }

  /**
   * Looks for a token in the database that matches the passing token.
   *
   * <p>If none is available, INVALID (-1) is returned. If it is, EXPIRED (-2) is returned. If it is
   * valid, the mail address of the corresponding user is validated and the token is deleted. Then
   * VALID (0) is returned.
   *
   * @param token the token to look for
   * @return returns a status code (INVALID, EXPIRED, VALID)
   */
  public int validateVerificationToken(String token) {
    try {
      verifyEmailUseCase.verifyEmail(token);
    } catch (InvalidVerificationTokenException e) {
      return TokenStatus.INVALID.statusCode();
    } catch (ExpiredVerificationTokenException e) {
      return TokenStatus.EXPIRED.statusCode();
    }
    return TokenStatus.VALID.statusCode();
  }
}
