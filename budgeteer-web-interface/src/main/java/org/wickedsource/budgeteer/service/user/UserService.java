package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import de.adesso.budgeteer.persistence.user.UserEntity;
import de.adesso.budgeteer.persistence.user.UserRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.UnknownEntityException;

@Service
@Log4j2
@Transactional
public class UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserMapper mapper;

  /**
   * Returns a list of all users that currently have access to the given project.
   *
   * @param projectId ID of the project whose users to load.
   * @return list of all users with access to the given project.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public List<User> getUsersInProject(long projectId) {
    return mapper.map(userRepository.findInProject(projectId));
  }

  /**
   * Returns a list of all users that currently DO NOT have access to the given project.
   *
   * @param projectId ID of the project
   * @return list of all users that currently DO NOT have access to the given project.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public List<User> getUsersNotInProject(long projectId) {
    return mapper.map(userRepository.findNotInProject(projectId));
  }

  /**
   * Removes the the access of the given user to the given project.
   *
   * @param projectId ID of the project for which to remove access
   * @param userId ID of the user whose access to remove.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public void removeUserFromProject(long projectId, long userId) {
    ProjectEntity project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new UnknownEntityException(ProjectEntity.class, projectId));
    UserEntity user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UnknownEntityException(UserEntity.class, userId));
    user.getAuthorizedProjects().remove(project);
    project.getAuthorizedUsers().remove(user);
  }

  /**
   * Adds the given user to the given project so that this user now has access to it.
   *
   * @param projectId ID of the project.
   * @param userId ID of the user.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public void addUserToProject(long projectId, long userId) {
    ProjectEntity project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new UnknownEntityException(ProjectEntity.class, projectId));
    UserEntity user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UnknownEntityException(UserEntity.class, userId));
    user.getAuthorizedProjects().add(project);
    project.getAuthorizedUsers().add(user);
  }

  /**
   * Checks a users login credentials.
   *
   * @param userName username or mail of the user to login
   * @param password plain text password of the user to login
   * @return a User object of the logged in user on successful login.
   * @throws InvalidLoginCredentialsException in case of invalid credentials
   */
  public User login(String userName, String password) throws InvalidLoginCredentialsException {
    var user = userRepository.findByName(userName);
    if (user == null) {
      throw new InvalidLoginCredentialsException();
    }
    var matches = passwordEncoder.matches(password, user.getPassword());
    if (!matches) {
      throw new InvalidLoginCredentialsException();
    }
    var upgradeEncoding = passwordEncoder.upgradeEncoding(user.getPassword());
    if (upgradeEncoding) {
      var rehashedPassword = passwordEncoder.encode(password);
      user.setPassword(rehashedPassword);
      userRepository.save(user);
    }
    return mapper.map(user);
  }

  /**
   * Registers a new user to the Budgeteer application. If the chosen username is already in-use, an
   * UsernameAlreadyInUseException is thrown. If the chosen mail address is already in-use, a
   * MailAlreadyInUseException is thrown. An event is triggered to signal the application that a new
   * user has been registered.
   *
   * @param username the users name
   * @param password the users password
   */
  public void registerUser(String username, String password) throws UsernameAlreadyInUseException {
    if (userRepository.findByName(username) != null) {
      throw new UsernameAlreadyInUseException();
    }
    UserEntity user = new UserEntity();
    user.setName(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }

  /**
   * Checks a password for matching the registered password.
   *
   * @param id the ID of the user
   * @param password password to be checked
   * @return true if the password matches the user, false if not
   */
  public boolean checkPassword(long id, String password) {
    var user = userRepository.findById(id).orElseThrow(RuntimeException::new);
    return passwordEncoder.matches(password, user.getPassword());
  }

  /**
   * Creates an EditUserData for a user to be able to edit him easy.
   *
   * @param id the ID of the user
   * @return EditUserData for editing the user
   */
  public EditUserData loadUserToEdit(long id) {
    UserEntity userEntity =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UnknownEntityException(UserEntity.class, id));
    EditUserData editUserData = new EditUserData();
    editUserData.setId(userEntity.getId());
    editUserData.setName(userEntity.getName());
    editUserData.setPassword(userEntity.getPassword());
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
   * @throws UsernameAlreadyInUseException
   */
  public void saveUser(EditUserData data, boolean changePassword)
      throws UsernameAlreadyInUseException {
    var testEntity = userRepository.findByName(data.getName());
    if (testEntity != null && testEntity.getId() != data.getId()) {
      throw new UsernameAlreadyInUseException();
    }

    var userEntity = userRepository.findById(data.getId()).orElseThrow(RuntimeException::new);
    userEntity.setId(data.getId());
    userEntity.setName(data.getName());

    if (changePassword) {
      userEntity.setPassword(passwordEncoder.encode(data.getPassword()));
    }

    userRepository.save(userEntity);
  }

  /**
   * Searches for a user using the ID. If one is found, the user is returned, otherwise a
   * UserIdNotFoundException is thrown.
   *
   * @param id the ID to look for
   * @return returns the user found with the ID
   * @throws UserIdNotFoundException
   */
  public UserEntity getUserById(long id) throws UserIdNotFoundException {
    return userRepository.findById(id).orElseThrow(UserIdNotFoundException::new);
  }
}
