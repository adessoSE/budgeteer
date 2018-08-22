package org.wickedsource.budgeteer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.UnknownEntityException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final PasswordHasher passwordHasher;

    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, ProjectRepository projectRepository, PasswordHasher passwordHasher, UserMapper mapper) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.passwordHasher = passwordHasher;
        this.mapper = mapper;
    }

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
     * @param userId    ID of the user whose access to remove.
     */
    @PreAuthorize("canReadProject(#projectId)")
    public void removeUserFromProject(long projectId, long userId) {
        Optional<ProjectEntity> project = projectRepository.findById(projectId);
        if (!project.isPresent()) {
            throw new UnknownEntityException(ProjectEntity.class, projectId);
        }
        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UnknownEntityException(UserEntity.class, userId);
        }
        user.get().getAuthorizedProjects().remove(project.get());
        project.get().getAuthorizedUsers().remove(user.get());
    }

    /**
     * Adds the given user to the given project so that this user now has access to it.
     *
     * @param projectId ID of the project.
     * @param userId    ID of the user.
     */
    @PreAuthorize("canReadProject(#projectId)")
    public void addUserToProject(long projectId, long userId) {
        Optional<ProjectEntity> project = projectRepository.findById(projectId);
        if (!project.isPresent()) {
            throw new UnknownEntityException(ProjectEntity.class, projectId);
        }
        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UnknownEntityException(UserEntity.class, userId);
        }
        user.get().getAuthorizedProjects().add(project.get());
        project.get().getAuthorizedUsers().add(user.get());
    }

    /**
     * Checks a users login credentials.
     *
     * @param username username of the user to login
     * @param password plain text password of the user to login
     * @return a User object of the logged in user on successful login.
     * @throws InvalidLoginCredentialsException in case of invalid credentials
     */
    public User login(String username, String password) throws InvalidLoginCredentialsException {
        UserEntity entity = userRepository.findByNameAndPassword(username, passwordHasher.hash(password));
        if (entity == null) {
            throw new InvalidLoginCredentialsException();
        }
        return mapper.map(entity);
    }

    /**
     * Login without password if using Keycloak
     */
    public User login(String username) {
        UserEntity userEntity = userRepository.findByName(username);
        if (userEntity == null) {
            registerUser(username);
            userEntity = userRepository.findByName(username);
        }
        return mapper.map(userEntity);
    }

    /**
     * Registers a new user to the Budgeteer application.
     * If the chosen username is already in-use, an UsernameAlreadyInUseException is thrown.
     *
     * @param username the users name
     * @param password the users password
     */
    public void registerUser(String username, String password) throws UsernameAlreadyInUseException {
        if (userRepository.findByName(username) == null) {
            UserEntity user = new UserEntity();
            user.setName(username);
            user.setPassword(passwordHasher.hash(password));
            userRepository.save(user);
        } else {
            throw new UsernameAlreadyInUseException();
        }
    }

    /**
     * Register user without password if using Keycloak
     */
    private void registerUser(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(username);
        userEntity.setPassword("password"); // dummy password
        userRepository.save(userEntity);
    }
}
