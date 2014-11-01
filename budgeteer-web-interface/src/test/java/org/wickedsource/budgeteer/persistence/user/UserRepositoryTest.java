package org.wickedsource.budgeteer.persistence.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.persistence.RepositoryTestConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testFindByNameAndPassword() {
        UserEntity user = new UserEntity();
        user.setName("name");
        user.setPassword("password");
        repository.save(user);

        UserEntity result = repository.findByNameAndPassword("name", "password");
        Assert.assertNotNull(result);
    }

}
