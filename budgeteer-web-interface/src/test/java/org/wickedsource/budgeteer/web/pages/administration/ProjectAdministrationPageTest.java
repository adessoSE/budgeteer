package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ProjectAdministrationPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Test
    void test() {
        WicketTester tester = getTester();
        when(userService.getUsersInProject(1L)).thenReturn(getUsersInProject());
        when(projectService.findProjectById(anyLong())).thenReturn(getNewProject());
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    private Project getNewProject() {
        return new Project(1L, null, null, "TestProject");
    }

    private List<User> getUsersInProject() {
        List<User> users = new ArrayList<User>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User " + i);
            users.add(user);
        }
        return users;
    }

    @Override
    protected void setupTest() {

    }
}
