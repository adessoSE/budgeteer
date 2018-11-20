package org.wickedsource.budgeteer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.*;
import org.wickedsource.budgeteer.service.UnknownEntityException;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${budgeteer.mail.activate}")
    private String mailActivated;

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
     * @param user     username or mail of the user to login
     * @param password plain text password of the user to login
     * @return a User object of the logged in user on successful login.
     * @throws InvalidLoginCredentialsException in case of invalid credentials
     */
    public User login(String user, String password) throws InvalidLoginCredentialsException {
        UserEntity entity = userRepository.findByNameOrMailAndPassword(user, passwordHasher.hash(password));

        if (entity == null)
            throw new InvalidLoginCredentialsException();

        if (entity.getMailVerified() == null)
            entity.setMailVerified(false);

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
     * If the chosen mail address is already in-use, a MailAlreadyInUseException is thrown.
     * An event is triggered to signal the application that a new user has been registered.
     *
     * @param username the users name
     * @param mail     the users mail address
     * @param password the users password
     */
    public void registerUser(String username, String mail, String password) throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        if (userRepository.findByName(username) != null) {
            throw new UsernameAlreadyInUseException();
        } else if (userRepository.findByMail(mail) != null) {
            throw new MailAlreadyInUseException();
        } else {
            UserEntity user = new UserEntity();
            user.setName(username);
            user.setMail(mail);
            user.setPassword(passwordHasher.hash(password));
            user.setRoles(new HashMap<>());
            userRepository.save(user);
            if (!mail.equals("") && Boolean.valueOf(mailActivated))
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
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

    public List<User> getAllUsers() {
        List<UserEntity> result = new ArrayList<>();
        for(UserEntity e : userRepository.findAll()){
            result.add(e);
        }
        return mapper.map(result);
    }

    public void removeUser(long id) {
        //Remove the user from all projects
        for (ProjectEntity e : projectRepository.findAll()) {
            this.removeUserFromProject(e.getId(), id);
        }

        userRepository.delete(id);
    }

    public void addRoleToUser(Long userId, Long projectID, UserRole role) {
        UserEntity user = userRepository.findById(userId);
        if(user.getRoles() == null){
            user.setRoles(new HashMap<>());
        }
        if(user.getRoles().get(projectID) == null || user.getRoles().get(projectID).size() == 0){
            user.getRoles().put(projectID, new ArrayList<>(Collections.singleton(role)));
        }else{
            List<UserRole> newRoles = user.getRoles().get(projectID);
            if(!newRoles.contains(role)) {
                newRoles.add(role);
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
            List<UserRole> newRoles = user.getRoles().get(projectID);
            newRoles.remove(role);
            user.getRoles().put(projectID, newRoles);
            userRepository.save(user);
        }
    }

    public List<User> getAllAdmins(){
        List<User> result = new ArrayList<>();
        for(UserEntity u : userRepository.findAll()){
            if(u.getGlobalRole().equals(UserRole.ADMIN)){
                result.add(mapper.map(u));
            }
        }
        return result;
    }

    public void setGlobalRoleForUser(Long userId, UserRole role){
        if(userId != 0) {
            UserEntity entity = userRepository.findById(userId);
            entity.setGlobalRole(role);
            userRepository.save(entity);
        }
    }

    public void setUserPassword(Long userId, String password){
        if(userId != 0) {
            UserEntity entity = userRepository.findById(userId);
            entity.setPassword(passwordHasher.hash(password));
            userRepository.save(entity);
        }
    }

    /**
     * Checks a password for matching the registered password.
     *
     * @param id       the ID of the user
     * @param password password to be checked
     * @return true if the password matches the user, false if not
     */
    public boolean checkPassword(long id, String password) {
        UserEntity entity = userRepository.findOne(id);
        return entity.getPassword().equals(passwordHasher.hash(password));
    }

    /**
     * Triggers an OnForgotPasswordEvent for the given user with the passed mail address.
     *
     * @param mail the users mail address
     * @throws MailNotFoundException
     * @throws MailNotVerifiedException
     */
    public void resetPassword(String mail) throws MailNotFoundException, MailNotVerifiedException {
        UserEntity userEntity = userRepository.findByMail(mail);

        if (userEntity == null) {
            throw new MailNotFoundException();
        } else if (!userEntity.getMailVerified()) {
            throw new MailNotVerifiedException();
        } else if (Boolean.valueOf(mailActivated)) {
            applicationEventPublisher.publishEvent(new OnForgotPasswordEvent(userEntity));
        }
    }

    /**
     * Creates an EditUserData for a user to be able to edit him easy.
     *
     * @param id the ID of the user
     * @return EditUserData for editing the user
     */
    public EditUserData loadUserToEdit(long id) {
        UserEntity userEntity = userRepository.findOne(id);

        if (userEntity == null)
            throw new UnknownEntityException(UserEntity.class, id);

        EditUserData editUserData = new EditUserData();
        editUserData.setId(userEntity.getId());
        editUserData.setName(userEntity.getName());
        editUserData.setMail(userEntity.getMail());
        editUserData.setPassword(userEntity.getPassword());
        return editUserData;
    }

    /**
     * Saves a user with the data passed.
     * <p>
     * If the name already exists, an UsernameAlreadyInUseException is thrown.
     * If the mail address already exists, a MailAlreadyInUseException is thrown.
     * <p>
     * If the user changes his mail address, getMailVerified is set to false, because the user has to verify the new mail address again.
     * If the user changes his password, the new password is hashed and then saved.
     *
     * @param data           the new data for the user
     * @param changePassword specifies if the password should also be changed
     * @return true if the mail address is verified, otherwise false
     * @throws UsernameAlreadyInUseException
     * @throws MailAlreadyInUseException
     */
    public boolean saveUser(EditUserData data, boolean changePassword) throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        assert data != null;
        UserEntity userEntity = new UserEntity();

        UserEntity testEntity = userRepository.findByName(data.getName());
        if (testEntity != null)
            if (testEntity.getId() != data.getId())
                throw new UsernameAlreadyInUseException();

        testEntity = userRepository.findByMail(data.getMail());
        if (testEntity != null)
            if (testEntity.getId() != data.getId())
                throw new MailAlreadyInUseException();

        userEntity = userRepository.findOne(data.getId());

        testEntity = userRepository.findOne(data.getId());
        if (testEntity != null)
            if (testEntity.getMail() == null)
                userEntity.setMailVerified(false);
            else if (!testEntity.getMail().equals(data.getMail()))
                userEntity.setMailVerified(false);

        userEntity.setId(data.getId());
        userEntity.setName(data.getName());
        userEntity.setMail(data.getMail());

        if (changePassword)
            userEntity.setPassword(passwordHasher.hash(data.getPassword()));
        else
            userEntity.setPassword(data.getPassword());

        userRepository.save(userEntity);

        return userEntity.getMailVerified();
    }

    /**
     * Creates a token for a specific user.
     *
     * @param userEntity the specific user
     * @param token      a token, which usually contains a random UUID
     */
    public VerificationToken createVerificationTokenForUser(UserEntity userEntity, String token) {
        VerificationToken verificationToken = new VerificationToken(userEntity, token);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    /**
     * Triggers an OnRegistrationCompleteEvent if an user changes his mail address.
     * An existing token is deleted to create a new one.
     *
     * @param userEntity the specific user
     */
    public void createNewVerificationTokenForUser(UserEntity userEntity) {
        VerificationToken verificationToken = verificationTokenRepository.findByUser(userEntity);
        if (verificationToken != null)
            verificationTokenRepository.delete(verificationToken);
        if (Boolean.valueOf(mailActivated))
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity));
    }

    /**
     * Looks for a token in the database that matches the passing token.
     * <p>
     * If none is available, INVALID (-1) is returned.
     * If it is, EXPIRED (-2) is returned.
     * If it is valid, the mail address of the corresponding user is validated and the token is deleted.
     * Then VALID (0) is returned.
     *
     * @param token the token to look for
     * @return returns a status code (INVALID, EXPIRED, VALID)
     */
    public int validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null)
            return TokenStatus.INVALID.statusCode();

        UserEntity userEntity = verificationToken.getUserEntity();
        Calendar calendar = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return TokenStatus.EXPIRED.statusCode();
        }

        userEntity.setMailVerified(true);
        userRepository.save(userEntity);
        verificationTokenRepository.delete(verificationToken);
        return TokenStatus.VALID.statusCode();
    }

    /**
     * Searches for a user using a mail address.
     * If not found, a MailNotFoundException is thrown, otherwise the user is returned.
     *
     * @param mail the mail address to look for
     * @return returns the user found with the mail address
     * @throws MailNotFoundException
     */
    public UserEntity getUserByMail(String mail) throws MailNotFoundException {
        UserEntity userEntity = userRepository.findByMail(mail);

        if (userEntity == null)
            throw new MailNotFoundException();
        else
            return userEntity;
    }

    /**
     * Searches for a user using the ID.
     * If one is found, the user is returned, otherwise a UserIdNotFoundException is thrown.
     *
     * @param id the ID to look for
     * @return returns the user found with the ID
     * @throws UserIdNotFoundException
     */
    public UserEntity getUserById(long id) throws UserIdNotFoundException {
        UserEntity userEntity = userRepository.findOne(id);

        if (userEntity == null)
            throw new UserIdNotFoundException();
        else
            return userEntity;
    }

    /**
     * Deletes an existing token.
     * A new token is then generated for the corresponding user.
     *
     * @param userEntity the specific user
     * @param token      a token, which usually contains a random UUID
     */
    public ForgotPasswordToken createForgotPasswordTokenForUser(UserEntity userEntity, String token) {
        ForgotPasswordToken oldForgotPasswordToken = forgotPasswordTokenRepository.findByUser(userEntity);
        if (oldForgotPasswordToken != null)
            forgotPasswordTokenRepository.delete(oldForgotPasswordToken);

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(userEntity, token);
        forgotPasswordTokenRepository.save(forgotPasswordToken);
        return forgotPasswordToken;
    }

    /**
     * Looks for a token in the database that matches the passing token.
     * <p>
     * If none is available, INVALID (-1) is returned.
     * If it is expired, EXPIRED (-2) is returned.
     * If it is valid, the mail address of the corresponding user is validated and the token is deleted.
     * Then VALID (0) is returned.
     *
     * @param token the token to look for
     * @return returns a status code (INVALID, EXPIRED, VALID)
     */
    public int validateForgotPasswordToken(String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);

        if (forgotPasswordToken == null)
            return TokenStatus.INVALID.statusCode();

        Calendar calendar = Calendar.getInstance();

        if ((forgotPasswordToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return TokenStatus.EXPIRED.statusCode();
        }

        return TokenStatus.VALID.statusCode();
    }

    /**
     * Searches for a user using a ForgotPasswordToken.
     * If one is found, the user is returned, otherwise null is returned.
     *
     * @param forgotPasswordTokenString the ForgotPasswordToken to look for
     * @return returns the user found with the ForgotPasswordToken
     */
    public UserEntity getUserByForgotPasswordToken(String forgotPasswordTokenString) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(forgotPasswordTokenString);
        if (forgotPasswordToken != null)
            return forgotPasswordToken.getUserEntity();
        else
            return null;
    }

    /**
     * Deletes the passed token if it exists.
     *
     * @param token the token to be deleted
     */
    public void deleteForgotPasswordToken(String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);

        if (forgotPasswordToken != null)
            forgotPasswordTokenRepository.delete(forgotPasswordToken);
    }

    public void setUserEmail(long id, String email) throws MailAlreadyInUseException{
        if (userRepository.findByMail(email) != null) {
            throw new MailAlreadyInUseException();
        } else {
            UserEntity user = userRepository.findById(id);
            user.setMail(email);
            userRepository.save(user);
            if (!email.equals("") && Boolean.valueOf(mailActivated)) {
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
            }
        }
    }
}