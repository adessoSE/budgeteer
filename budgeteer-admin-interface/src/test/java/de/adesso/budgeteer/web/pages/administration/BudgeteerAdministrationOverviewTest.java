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
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.administration.Project;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class BudgeteerAdministrationOverviewTest extends AbstractWebTestTemplate {

    @Test
    void test() {
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

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);;

        //Test
        FormTester formTester = tester.newFormTester("userList:0:passwordResetField");
        formTester.setValue("setPasswordTextBox", "password1");
        formTester.submit();

        formTester = tester.newFormTester("userList:1:passwordResetField");
        formTester.setValue("setPasswordTextBox", "password2");
        formTester.submit();

        Mockito.verify(userServiceMock, times(1)).setUserPassword(1L, "password1");
        Mockito.verify(userServiceMock, times(1)).setUserPassword(2L, "password2");
    }

    @Test
    void testSetEmailField() throws Exception {
        //set up
        User user1 = new User();
        user1.setName("Maxim");
        user1.setId(1L);
        user1.setMail("maxim@adesso.de");
        user1.setGlobalRole(UserRole.USER);

        User user2 = new User();
        user2.setName("Kilian");
        user2.setId(2L);
        user2.setMail("kilian@adesso.de");
        user2.setGlobalRole(UserRole.USER);

        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        WicketTester tester = getTester();
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        FormTester formTester = tester.newFormTester("userList:0:emailResetField");
        formTester.setValue("setEmailTextBox", "email1@adesso.de");
        formTester.submit();

        formTester = tester.newFormTester("userList:1:emailResetField");
        formTester.setValue("setEmailTextBox", "email2@adesso.de");
        formTester.submit();

        Mockito.verify(userServiceMock, times(1)).setUserEmail(1L, "email1@adesso.de");
        Mockito.verify(userServiceMock, times(1)).setUserEmail(2L, "email2@adesso.de");
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
    void testRemoveUserButtonOnlyVisibleWhenTwoAdminsExist() {
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

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Button should be visible as there are two admin users
        Assertions.assertTrue(tester.getComponentFromLastRenderedPage("userList:0:deleteUserButton").isVisible());

        //Set the second user to have only user privileges and start the page again
        user2.setGlobalRole(UserRole.USER);
        page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        tester.assertInvisible("userList:0:deleteUserButton");
    }

    @Test
    void testUserRoleCheckboxesOnlyVisibleWhenTwoAdminsExist() {
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

        WicketTester tester = getTester();

        ((BudgeteerSession)tester.getSession()).setLoggedInUser(user1);
        WebPage page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        //Test
        //Dropdown should be visible as there are two admin users
        Assertions.assertTrue(tester.getComponentFromLastRenderedPage("userList:0:checkBoxContainer").isVisible());

        //Set the second user to have only user privileges and start the page again
        user2.setGlobalRole(UserRole.USER);
        page = new BudgeteerAdministrationOverview();
        tester.startPage(page);

        tester.assertInvisible("userList:0:checkBoxContainer");
    }

    @Test
    void testProjectListEditButton() {
        //set up
        ProjectBaseData project1 = new ProjectBaseData();
        project1.setId(1L);
        project1.setName("project1");

        ProjectBaseData project2 = new ProjectBaseData();
        project2.setId(2L);
        project2.setName("project2");

        when(userServiceMock.getUsersInProject(anyLong())).thenReturn(new ArrayList<>());
        when(projectServiceMock.getAllProjects()).thenReturn(Arrays.asList(project1, project2));
        when(projectServiceMock.findProjectById(1L))
                .thenReturn(new Project(project1.getId(), new Date(), new Date(), project1.getName()));
        when(projectServiceMock.findProjectById(2L))
                .thenReturn(new Project(project2.getId(), new Date(), new Date(), project2.getName()));

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

        ProjectBaseData project2 = new ProjectBaseData();
        project2.setId(2L);
        project2.setName("project2");

        when(projectServiceMock.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

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
