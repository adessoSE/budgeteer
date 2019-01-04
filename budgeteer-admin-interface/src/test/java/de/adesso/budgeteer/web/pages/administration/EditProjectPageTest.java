package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.administration.Project;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditProjectPageTest extends AbstractWebTestTemplate {


    @Test
    void test() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);
        Map<Long, List<UserRole>> user1projectRoles = new HashMap<>();
        user1projectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.ADMIN)));
        user1.setRoles(user1projectRoles);

        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");

        when(userServiceMock.getUsersInProject(anyLong())).thenReturn(Collections.singletonList(user1));
        when(projectServiceMock.getAllProjects()).thenReturn(Collections.singletonList(project1));
        when(projectServiceMock.findProjectById(1L))
                .thenReturn(new Project(project1.getId(), new Date(), new Date(), project1.getName()));

        WicketTester tester = getTester();
        tester.startPage(EditProjectPage.class, EditProjectPage.createParameters(1));
        tester.assertRenderedPage(EditProjectPage.class);
    }

    @Test
    void testDropdownAndDeleteButtonOnlyAppearWhenTwoAdminsArePresent() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);
        Map<Long, List<UserRole>> user1projectRoles = new HashMap<>();
        user1projectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.ADMIN)));
        user1.setRoles(user1projectRoles);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);
        Map<Long, List<UserRole>> user2projectRoles = new HashMap<>();
        user2projectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.USER)));
        user2.setRoles(user2projectRoles);

        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");


        when(userServiceMock.getUsersInProject(anyLong())).thenReturn(Arrays.asList(user1, user2));
        when(projectServiceMock.getAllProjects()).thenReturn(Collections.singletonList(project1));
        when(projectServiceMock.findProjectById(1L))
                .thenReturn(new Project(project1.getId(), new Date(), new Date(), project1.getName()));

        WicketTester tester = getTester();
        tester.startPage(EditProjectPage.class, EditProjectPage.createParameters(1));

        //Invisible for the admin
        tester.assertInvisible("userList:0:deleteButton");
        tester.assertInvisible("userList:0:roleDropdown");

        //Visible for the user
        tester.assertVisible("userList:1:deleteButton");
        tester.assertVisible("userList:1:roleDropdown");
    }

    @Test
    void testDropdownAndDeleteButtonAppearWhenTwoAdminsArePresent() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);
        Map<Long, List<UserRole>> user1projectRoles = new HashMap<>();
        user1projectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.ADMIN)));
        user1.setRoles(user1projectRoles);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);
        Map<Long, List<UserRole>> user2projectRoles = new HashMap<>();
        user2projectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.ADMIN)));
        user2.setRoles(user2projectRoles);

        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");

        when(userServiceMock.getUsersInProject(anyLong())).thenReturn(Arrays.asList(user1, user2));
        when(projectServiceMock.getAllProjects()).thenReturn(Collections.singletonList(project1));
        when(projectServiceMock.findProjectById(1L))
                .thenReturn(new Project(project1.getId(), new Date(), new Date(), project1.getName()));

        WicketTester tester = getTester();
        tester.startPage(EditProjectPage.class, EditProjectPage.createParameters(1));

        tester.assertVisible("userList:0:deleteButton");
        tester.assertVisible("userList:0:roleDropdown");
        tester.assertVisible("userList:1:deleteButton");
        tester.assertVisible("userList:1:roleDropdown");
    }

    @Override
    protected void setupTest() {
        BudgeteerSession.get().setProjectId(1L);
    }
}
