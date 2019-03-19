package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

import static org.mockito.Matchers.anyLong;

import static org.mockito.ArgumentMatchers.any;

public class ProjectAdministrationPageTest extends AbstractWebTestTemplate {

    @Test
    void renderTest() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    @Test
    void testFormSubmitError() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        FormTester formTester = tester.newFormTester("projectChangeForm");
        formTester.setValue("projectTitle", "");
        tester.executeAjaxEvent("projectChangeForm:projectTitle", "change");
        formTester.setValue("projectStart", "01.01.2019 - 28.02.2019");
        tester.executeAjaxEvent("projectChangeForm:projectStart", "change");
        tester.clickLink("projectChangeForm:submitLink");
        tester.assertRenderedPage(ProjectAdministrationPage.class);
        tester.assertFeedbackMessages(FeedbackMessage::isError, tester.getLastRenderedPage().getString("error.no.name"));
        Mockito.verify(projectServiceMock, Mockito.times(0)).save(any());
    }

    @Test
    void testFormSubmitSuccess() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        FormTester formTester = tester.newFormTester("projectChangeForm");
        formTester.setValue("projectTitle", "test");
        tester.executeAjaxEvent("projectChangeForm:projectTitle", "change");
        formTester.setValue("projectStart", "01.01.2019 - 28.02.2019");
        tester.executeAjaxEvent("projectChangeForm:projectStart", "change");
        tester.clickLink("projectChangeForm:submitLink");
        tester.assertRenderedPage(ProjectAdministrationPage.class);
        tester.assertFeedbackMessages(FeedbackMessage::isSuccess, tester.getLastRenderedPage().getString("project.saved"));
        Mockito.verify(projectServiceMock, Mockito.times(1)).save(any());
    }

    @Test
    void testDeleteButtonInvisibleWhenOneUserIsAdmin() {
        WicketTester tester = getTester();

        //Add the admin role to the first user
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);


        //See if the delete button is visible or not
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertInvisible("projectUsers:userList:0:deleteButton");
        tester.assertVisible("projectUsers:userList:1:deleteButton");
    }

    @Test
    void testDeleteButtonVisibleWhenTwoAdminsExist() {
        WicketTester tester = getTester();

        //Add the admin role to the first 2 users
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);
        userServiceMock.getUsersInProject(1L).get(1).getRoles(1L).add(UserRole.ADMIN);


        //See if the delete button is visible or not
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertVisible("projectUsers:userList:0:deleteButton");
        tester.assertVisible("projectUsers:userList:1:deleteButton");
    }

    @Test
    void testDropdownInvisibleWhenOneUserIsAdmin() {
        WicketTester tester = getTester();

        //Add the admin role to the first user
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);

        //See if the delete button is visible or not
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertInvisible("projectUsers:userList:0:adminCheckbox:roleCheckBox");
        tester.assertVisible("projectUsers:userList:1:adminCheckbox:roleCheckBox");
    }

    @Test
    void testDropdownVisibleWhenTwoAdminsExist() {
        WicketTester tester = getTester();

        //Add the admin role to the first 2 users
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);
        userServiceMock.getUsersInProject(1L).get(1).getRoles(1L).add(UserRole.ADMIN);


        //See if the delete button is visible or not
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertVisible("projectUsers:userList:0:adminCheckbox:roleCheckBox");
        tester.assertVisible("projectUsers:userList:1:adminCheckbox:roleCheckBox");
    }

    @Test
    void testDeleteDialogConfirmWhenDeleteButtonIsClicked() {
        WicketTester tester = getTester();

        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);
        userServiceMock.getUsersInProject(1L).get(1).getRoles(1L).add(UserRole.USER);

        //Verify the correct behaviour when pressing yes
        tester.startPage(ProjectAdministrationPage.class);
        tester.clickLink("projectUsers:userList:1:deleteButton");
        tester.assertRenderedPage(DeleteDialog.class);
        tester.clickLink("yesButton");
        Mockito.verify(userServiceMock, Mockito.times(1)).removeUserFromProject(anyLong(), anyLong());
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    @Test
    void testDeleteDialogDeclineWhenDeleteButtonIsClicked() {
        WicketTester tester = getTester();

        //Add the admin role only to the second user
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.USER);
        userServiceMock.getUsersInProject(1L).get(1).getRoles(1L).add(UserRole.ADMIN);

        //Verify the correct behaviour when pressing no
        tester.startPage(ProjectAdministrationPage.class);
        tester.clickLink("projectUsers:userList:0:deleteButton");
        tester.assertRenderedPage(DeleteDialog.class);
        tester.clickLink("noButton");
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    @Test
    void testDeleteDialogConfirmWhenDeleteButtonIsClickedForCurrentUser() {
        WicketTester tester = getTester();

        //Add the admin role to the first 2 users
        BudgeteerSession.get().setProjectId(1L);
        userServiceMock.getUsersInProject(1L).get(0).getRoles(1L).add(UserRole.ADMIN);
        userServiceMock.getUsersInProject(1L).get(1).getRoles(1L).add(UserRole.ADMIN);
        BudgeteerSession.get().setLoggedInUser(userServiceMock.getUsersInProject(1L).get(0));

        //Verify the correct behaviour when pressing yes
        tester.startPage(ProjectAdministrationPage.class);
        tester.clickLink("projectUsers:userList:0:deleteButton");
        tester.assertRenderedPage(DeleteDialog.class);
        tester.clickLink("yesButton");
        Mockito.verify(userServiceMock, Mockito.times(1)).removeUserFromProject(anyLong(), anyLong());
        tester.assertRenderedPage(SelectProjectPage.class);
    }

    @Test
    void testDeleteDialogProjectButton() {
        WicketTester tester = getTester();

        //Verify the correct behaviour when pressing yes
        tester.startPage(ProjectAdministrationPage.class);
        tester.clickLink("deleteProjectButton");
        tester.assertRenderedPage(DeleteDialog.class);
        tester.clickLink("yesButton");
        Mockito.verify(projectServiceMock, Mockito.times(1)).deleteProject(anyLong());
        tester.assertRenderedPage(SelectProjectPage.class);
    }

    @Override
    protected void setupTest() {
        Mockito.clearInvocations(userServiceMock);
        BudgeteerSession.get().setLoggedInUser(userServiceMock.getUsersInProject(1L).get(0));
    }
}