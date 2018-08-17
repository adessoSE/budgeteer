package org.wickedsource.budgeteer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.UnknownEntityException;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private UserMapper mapper;

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
        ProjectEntity project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new UnknownEntityException(ProjectEntity.class, projectId);
        }
        UserEntity user = userRepository.findOne(userId);
        if (user == null) {
            throw new UnknownEntityException(UserEntity.class, userId);
        }
        user.getAuthorizedProjects().remove(project);
        project.getAuthorizedUsers().remove(user);
        removeAllRolesFromUser(user.getId(), projectId);
    }

    /**
     * Adds the given user to the given project so that this user now has access to it.
     *
     * @param projectId ID of the project.
     * @param userId    ID of the user.
     */
    @PreAuthorize("canReadProject(#projectId)")
    public void addUserToProject(long projectId, long userId) {
        ProjectEntity project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new UnknownEntityException(ProjectEntity.class, projectId);
        }
        UserEntity user = userRepository.findOne(userId);
        if (user == null) {
            throw new UnknownEntityException(UserEntity.class, userId);
        }
        user.getAuthorizedProjects().add(project);
        project.getAuthorizedUsers().add(user);
        addRoleToUser(user.getId(), projectId, UserRole.USER);
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
            user.setRoles(new HashMap<>());
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
        userEntity.setRoles(new HashMap<>());
        userRepository.save(userEntity);
    }


    public void addRoleToUser(Long userId, Long projectID, UserRole role) {
        UserEntity user = userRepository.findById(userId);
        if(user.getRoles().get(projectID) == null || user.getRoles().get(projectID).size() == 0){
            user.getRoles().put(projectID, new ArrayList<>(Collections.singleton(role.toString())));
        }else{
            ArrayList<String> newRoles = user.getRoles().get(projectID);
            if(!newRoles.contains(role.toString())) {
                newRoles.add(role.toString());
                user.getRoles().put(projectID, newRoles);
            }
        }
        userRepository.save(user);
    }

    public void removeAllRolesFromUser(Long userId, Long projectID) {
        UserEntity user = userRepository.findById(userId);
        user.getRoles().remove(projectID);
        userRepository.save(user);
    }

    private void removeRoleFromUser(long userId, long projectID, UserRole role) {
        UserEntity user = userRepository.findById(userId);
        if(user.getRoles().get(projectID) != null && user.getRoles().get(projectID).size() != 0){
            ArrayList<String> newRoles = user.getRoles().get(projectID);
            newRoles.remove(role.toString());
            user.getRoles().put(projectID, newRoles);
            userRepository.save(user);
        }
    }
}
