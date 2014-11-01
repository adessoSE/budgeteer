package org.wickedsource.budgeteer.service.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml", "classpath:spring-repository-mock.xml"})
public class UserServiceTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    public void testRegisterUser() {
        service.registerUser("User", "Password");
        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testLoginSuccess() throws InvalidLoginCredentialsException {
        when(repository.findByNameAndPassword("user", passwordHasher.hash("password"))).thenReturn(createUserEntity());
        User user = service.login("user", "password");
        Assert.assertNotNull(user);
    }

    @Test(expected = InvalidLoginCredentialsException.class)
    public void testLoginFail() throws InvalidLoginCredentialsException {
        when(repository.findByNameAndPassword("user", passwordHasher.hash("password"))).thenReturn(null);
        service.login("user", "password");
    }

    private UserEntity createUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1l);
        user.setName("user");
        user.setPassword(passwordHasher.hash("password"));
        return user;
    }
}
