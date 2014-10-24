package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class ProjectAdministrationPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService service;

    @Test
    public void test() {
        WicketTester tester = getTester();
        when(service.getUsersInProject(1l)).thenReturn(getUsersInProject());
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    public List<User> getUsersInProject() {
        List<User> users = new ArrayList<User>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User " + i);
            users.add(user);
        }
        return users;
    }

}
