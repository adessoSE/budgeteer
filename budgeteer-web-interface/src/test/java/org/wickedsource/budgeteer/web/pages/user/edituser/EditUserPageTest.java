package org.wickedsource.budgeteer.web.pages.user.edituser;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.PasswordHasher;
import org.wickedsource.budgeteer.service.user.UserIdNotFoundException;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditUserPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BeforeEach
    void setUpMocks() throws UserIdNotFoundException {
        when(userService.loadUserToEdit(1L)).thenReturn(new EditUserData(1L, "test", "test@budgeteer.local", "password", null, null, new ProjectBaseData(), new Date()));
        when(userService.getUserById(anyLong())).thenReturn(new UserEntity(1L, "test", new PasswordHasher().hash("currentPassword"), "test@budgeteer.local", false, null, null, new Date()));
        ProjectBaseData projectBaseData = new ProjectBaseData();
        projectBaseData.setId(1L);
        projectBaseData.setName("project1");
        when(projectService.getDefaultProjectForUser(1L)).thenReturn(projectBaseData);
    }

    @Test
    void test() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        tester.assertRenderedPage(EditUserPage.class);
    }

    @Test
    void testSubmitNoEmail() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("mail", null);
        formTester.submit();
        tester.assertErrorMessages(formTester.getForm().getString("form.mail.Required"));
    }

    @Test
    void testSubmitSameEmail() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("mail", "test@budgeteer.local");
        formTester.submit("submitButton");
        tester.assertFeedbackMessages(null, formTester.getForm().getString("message.success"));
    }

    @Test
    void testSubmit() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("mail", "test@different.local");
        formTester.submit("submitButton");
        tester.assertFeedbackMessages(null, formTester.getForm().getString("message.successVerification"));
    }

    @Test
    void testSubmitNoUsernames() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");

        formTester.setValue("usernameTextField", "");
        tester.executeAjaxEvent("form:usernameForm:usernameTextField", "change");
        formTester.setValue("usernameRetypedTextField", "");
        tester.executeAjaxEvent("form:usernameForm:usernameRetypedTextField", "change");

        tester.clickLink("form:usernameForm:submitButton2");
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitNoUsernameRetyped() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");

        formTester.setValue("usernameTextField", "name");
        tester.executeAjaxEvent("form:usernameForm:usernameTextField", "change");
        formTester.setValue("usernameRetypedTextField", "");
        tester.executeAjaxEvent("form:usernameForm:usernameRetypedTextField", "change");

        tester.clickLink("form:usernameForm:submitButton2");
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitNoUsername() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");

        formTester.setValue("usernameTextField", "");
        tester.executeAjaxEvent("form:usernameForm:usernameTextField", "change");
        formTester.setValue("usernameRetypedTextField", "name");
        tester.executeAjaxEvent("form:usernameForm:usernameRetypedTextField", "change");

        tester.clickLink("form:usernameForm:submitButton2");
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitUsernameSuccess() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");

        formTester.setValue("usernameTextField", "name");
        tester.executeAjaxEvent("form:usernameForm:usernameTextField", "change");
        formTester.setValue("usernameRetypedTextField", "name");
        tester.executeAjaxEvent("form:usernameForm:usernameRetypedTextField", "change");

        tester.clickLink("form:usernameForm:submitButton2");
        tester.assertFeedbackMessages(null, formTester.getForm().getString("message.success"));
    }

    @Test
    void testSubmitNoPasswords() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.setValue("currentPasswordTextField", "");
        tester.executeAjaxEvent("form:passwordForm:currentPasswordTextField", "change");
        formTester.setValue("passwordTextField", "");
        tester.executeAjaxEvent("form:passwordForm:passwordTextField", "change");
        formTester.setValue("passwordRetypedTextField", "");
        tester.executeAjaxEvent("form:passwordForm:passwordRetypedTextField", "change");

        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.currentPassword.Required"));
    }

    @Test
    void testSubmitNoCurrentPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.setValue("currentPasswordTextField", "");
        tester.executeAjaxEvent("form:passwordForm:currentPasswordTextField", "change");
        formTester.setValue("passwordTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordTextField", "change");
        formTester.setValue("passwordRetypedTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordRetypedTextField", "change");


        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.currentPassword.Required"));
    }

    @Test
    void testSubmitNoPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.setValue("currentPasswordTextField", "currentPassword");
        tester.executeAjaxEvent("form:passwordForm:currentPasswordTextField", "change");
        formTester.setValue("passwordTextField", "");
        tester.executeAjaxEvent("form:passwordForm:passwordTextField", "change");
        formTester.setValue("passwordRetypedTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordRetypedTextField", "change");


        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.newPassword.Required"));
    }

    @Test
    void testSubmitNoRetypedPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.setValue("currentPasswordTextField", "currentPassword");
        tester.executeAjaxEvent("form:passwordForm:currentPasswordTextField", "change");
        formTester.setValue("passwordTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordTextField", "change");
        formTester.setValue("passwordRetypedTextField", "");
        tester.executeAjaxEvent("form:passwordForm:passwordRetypedTextField", "change");

        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.newPasswordConfirmation.Required"));
    }


    @Test
    void testSubmitPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.setValue("currentPasswordTextField", "currentPassword");
        tester.executeAjaxEvent("form:passwordForm:currentPasswordTextField", "change");
        formTester.setValue("passwordTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordTextField", "change");
        formTester.setValue("passwordRetypedTextField", "newPassword");
        tester.executeAjaxEvent("form:passwordForm:passwordRetypedTextField", "change");

        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertFeedbackMessages(null, formTester.getForm().getString("message.success"));
    }


    @Override
    protected void setupTest() {
    }
}
