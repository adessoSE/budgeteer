package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UsernameAlreadyInUseException;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.administration.Project;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class BudgeteerAdministrationOverviewTest extends AbstractWebTestTemplate {

    @Test
    void renderTest() {
        WicketTester tester = getTester();
        tester.startPage(BudgeteerAdministrationOverview.class);
        tester.assertRenderedPage(BudgeteerAdministrationOverview.class);
    }

    @Test
    void testSetPasswordField() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Collections.singletonList(user1));
        when(userServiceMock.getAllAdmins()).thenReturn(new ArrayList<>());

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        FormTester formTester = tester.newFormTester("userList:0:passwordResetField");
        formTester.setValue("setPasswordTextBox", "password1");
        formTester.submit();

        Mockito.verify(userServiceMock, times(1)).setUserPassword(1L, "password1");
    }

    @Test
    void testSetEmailField() throws Exception {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Collections.singletonList(user1));
        when(userServiceMock.getAllAdmins()).thenReturn(new ArrayList<>());

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        FormTester formTester = tester.newFormTester("userList:0:emailResetField");
        formTester.setValue("setEmailTextBox", "email1@adesso.de");
        formTester.submit();


        Mockito.verify(userServiceMock, times(1)).setUserEmail(1L, "email1@adesso.de");
    }

    @Test
    void testSetUsernameField() throws UsernameAlreadyInUseException {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Collections.singletonList(user1));
        when(userServiceMock.getAllAdmins()).thenReturn(new ArrayList<>());

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        FormTester formTester = tester.newFormTester("userList:0:usernameResetField");
        formTester.setValue("setUsernameTextBox", "newUsername");
        formTester.submit();

        Mockito.verify(userServiceMock, times(1)).setUserUsername(1L, "newUsername");
    }

    @Test
    void testRemoveUserButton() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.ADMIN);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userServiceMock.getAllAdmins()).thenReturn(Arrays.asList(user1, user2));


        WicketTester tester = getTester();
        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        tester.getComponentFromLastRenderedPage("userList:0:deleteUserButton");
        tester.clickLink("userList:0:deleteUserButton", false);

        tester.assertRenderedPage(DeleteDialog.class);
    }

    @Test
    void testRemoveUserButtonVisibleWhenTwoAdminsExist() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.ADMIN);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userServiceMock.getAllAdmins()).thenReturn(Arrays.asList(user1, user2));

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Button should be visible as there are two admin users
        Assertions.assertTrue(tester.getComponentFromLastRenderedPage("userList:0:deleteUserButton").isVisible());
    }

    @Test
    void testRemoveUserButtonInvisibleWhenOnlyOneAdminExists() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userServiceMock.getAllAdmins()).thenReturn(Collections.singletonList(user1));

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Button should be visible as there are two admin users
        tester.assertInvisible("userList:0:deleteUserButton");
    }

    @Test
    void testAddAndRevokeRightVisibleWhenTwoAdminsExist() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.ADMIN);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userServiceMock.getAllAdmins()).thenReturn(Arrays.asList(user1, user2));

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Buttons should be visible as there are two admin users
        tester.assertInvisible("userList:0:makeUserAdmin");
        tester.assertVisible("userList:0:revokeAdminRights");
        tester.assertInvisible("userList:1:makeUserAdmin");
        tester.assertVisible("userList:1:revokeAdminRights");
    }

    @Test
    void testAddAndRevokeRightInvisibleWhenOnlyOneAdminExists() {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.ADMIN);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userServiceMock.getAllAdmins()).thenReturn(Collections.singletonList(user1));

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Buttons should be visible as there are two admin users
        tester.assertInvisible("userList:0:makeUserAdmin");
        tester.assertInvisible("userList:0:revokeAdminRights");
        tester.assertVisible("userList:1:makeUserAdmin");
        tester.assertInvisible("userList:1:revokeAdminRights");

        //Set the second user to have only user privileges and start the page again
        user2.setGlobalRole(UserRole.USER);
        when(userServiceMock.getAllAdmins()).thenReturn(Collections.singletonList(user1));
        page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        tester.assertInvisible("userList:0:makeUserAdmin");
        tester.assertInvisible("userList:0:revokeAdminRights");
    }

    @Test
    void testProjectListEditButton() {
        //set up
        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");

        when(userServiceMock.getUsersInProject(anyLong())).thenReturn(new ArrayList<>());
        when(projectServiceMock.findProjectById(1L))
                .thenReturn(new Project(project1.getId(), new Date(), new Date(), project1.getName()));

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        tester.clickLink("projectList:0:editProjectButton");
        tester.assertRenderedPage(EditProjectPage.class);
        Assertions.assertEquals("1", tester.getLastRenderedPage().getPageParameters().get("id").toString());
    }

    @Test
    void testProjectListDeleteButton() {
        //set up
        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");

        when(projectServiceMock.getAllProjects()).thenReturn(Collections.singletonList(project1));

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        tester.clickLink("projectList:0:deleteProjectButton");
        tester.assertRenderedPage(DeleteDialog.class);
    }

    @Override
    protected void setupTest() {
    }
}
