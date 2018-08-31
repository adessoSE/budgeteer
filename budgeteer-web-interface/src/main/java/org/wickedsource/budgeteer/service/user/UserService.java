package org.wickedsource.budgeteer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.*;
import org.wickedsource.budgeteer.service.UnknownEntityException;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

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
    public UserEntity registerUser(String username, String mail, String password) throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        if (userRepository.findByName(username) != null) {
            throw new UsernameAlreadyInUseException();
        } else if (userRepository.findByMail(mail) != null) {
            throw new MailAlreadyInUseException();
        } else {
            UserEntity user = new UserEntity();
            user.setName(username);
            user.setMail(mail);
            user.setPassword(passwordHasher.hash(password));
            userRepository.save(user);
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
            return user;
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

    public boolean checkPassword(long id, String password) {
        UserEntity entity = userRepository.findOne(id);

        if (entity.getPassword().equals(passwordHasher.hash(password)))
            return true;
        else
            return false;
    }

    public void resetPassword(String mail) throws MailNotFoundException, MailNotVerifiedException {
        UserEntity userEntity = userRepository.findByMail(mail);

        if (userEntity == null) {
            throw new MailNotFoundException();
        } else if (!userEntity.isMailVerified()) {
            throw new MailNotVerifiedException();
        } else {
            applicationEventPublisher.publishEvent(new OnForgotPasswordEvent(userRepository.findByMail(mail)));
        }
    }

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
            if (!testEntity.getMail().equals(data.getMail()))
                userEntity.setMailVerified(false);

        userEntity.setId(data.getId());
        userEntity.setName(data.getName());
        userEntity.setMail(data.getMail());

        if (changePassword)
            userEntity.setPassword(passwordHasher.hash(data.getPassword()));
        else
            userEntity.setPassword(data.getPassword());

        userRepository.save(userEntity);

        return userEntity.isMailVerified();
    }

    public EditUserData createEditUserDataFromUser(UserEntity userEntity) {
        EditUserData editUserData = new EditUserData();

        editUserData.setId(userEntity.getId());
        editUserData.setName(userEntity.getName());
        editUserData.setMail(userEntity.getMail());
        editUserData.setPassword(userEntity.getPassword());

        return editUserData;
    }

    public void createVerificationTokenForUser(UserEntity userEntity, String token) {
        VerificationToken verificationToken = new VerificationToken(userEntity, token);
        verificationTokenRepository.save(verificationToken);
    }

    public void createNewVerificationTokenForUser(UserEntity userEntity) {
        VerificationToken verificationToken = verificationTokenRepository.findByUser(userEntity);
        if (verificationToken != null)
            verificationTokenRepository.delete(verificationToken);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity));
    }

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

    public UserEntity getUserByMail(String mail) throws MailNotFoundException {
        UserEntity userEntity = userRepository.findByMail(mail);

        if (userEntity == null)
            throw new MailNotFoundException();
        else
            return userEntity;
    }

    public UserEntity getUserById(long id) throws UserIdNotFoundException {
        UserEntity userEntity = userRepository.findById(id);

        if (userEntity == null)
            throw new UserIdNotFoundException();
        else
            return userEntity;
    }

    public void createForgotPasswordTokenForUser(UserEntity userEntity, String token) {
        ForgotPasswordToken oldForgotPasswordToken = forgotPasswordTokenRepository.findByUser(userEntity);
        if (oldForgotPasswordToken != null)
            forgotPasswordTokenRepository.delete(oldForgotPasswordToken);

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(userEntity, token);
        forgotPasswordTokenRepository.save(forgotPasswordToken);
    }

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

    public UserEntity getUserByForgotPasswordToken(String forgotPasswordTokenString) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(forgotPasswordTokenString);
        if (forgotPasswordToken != null)
            return forgotPasswordToken.getUserEntity();
        else
            return null;
    }

    public void deleteForgotPasswordToken(String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);

        if (forgotPasswordToken != null)
            forgotPasswordTokenRepository.delete(forgotPasswordToken);
    }
}