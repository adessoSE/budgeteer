package org.wickedsource.budgeteer.web.pages.user.edituser;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;

import java.util.Date;

import static org.mockito.Mockito.when;

public class EditUserPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BeforeEach
    void setUpMocks() {
        when(userService.loadUserToEdit(1L)).thenReturn(new EditUserData(1L, "test", "test@budgeteer.local", "password", null, null, new ProjectBaseData(), new Date()));
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
        formTester.getForm().get("usernameTextField").setDefaultModelObject("");
        formTester.getForm().get("usernameRetypedTextField").setDefaultModelObject("");
        tester.clickLink("form:usernameForm:submitButton2", true);
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitNoUsernameRetyped() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");
        formTester.getForm().get("usernameTextField").setDefaultModelObject("name");
        formTester.getForm().get("usernameRetypedTextField").setDefaultModelObject("");
        tester.clickLink("form:usernameForm:submitButton2", true);
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitNoUsername() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");
        formTester.getForm().get("usernameTextField").setDefaultModelObject("");
        formTester.getForm().get("usernameRetypedTextField").setDefaultModelObject("name");
        tester.clickLink("form:usernameForm:submitButton2", true);
        tester.assertErrorMessages(formTester.getForm().getString("form.username.Required"));
    }

    @Test
    void testSubmitUsernameSuccess() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:usernameForm");
        formTester.getForm().get("usernameTextField").setDefaultModelObject("name");
        formTester.getForm().get("usernameRetypedTextField").setDefaultModelObject("name");
        tester.clickLink("form:usernameForm:submitButton2", true);
        tester.assertFeedbackMessages(null, formTester.getForm().getString("message.success"));
    }

    @Test
    void testSubmitNoPasswords() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");
        formTester.getForm().get("currentPasswordTextField").setDefaultModelObject("");
        formTester.getForm().get("passwordTextField").setDefaultModelObject("");
        formTester.getForm().get("passwordRetypedTextField").setDefaultModelObject("");
        tester.clickLink("form:passwordForm:submitButton3", true);
        tester.assertErrorMessages(formTester.getForm().getString("form.currentPassword.Required"));
    }

    @Test
    void testSubmitNoCurrentPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");
        formTester.getForm().get("currentPasswordTextField").setDefaultModelObject("");
        formTester.getForm().get("passwordTextField").setDefaultModelObject("newPassword");
        formTester.getForm().get("passwordRetypedTextField").setDefaultModelObject("newPassword");
        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.currentPassword.Required"));
    }

    @Test
    void testSubmitNoPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");

        formTester.getForm().get("currentPasswordTextField").setDefaultModelObject("currentPassword");
        formTester.getForm().get("passwordTextField").setDefaultModelObject("");
        formTester.getForm().get("passwordRetypedTextField").setDefaultModelObject("newPassword");

        tester.executeAjaxEvent(formTester.getForm().get("currentPasswordTextField"), "change");
        tester.executeAjaxEvent(formTester.getForm().get("passwordTextField"), "change");
        tester.executeAjaxEvent(formTester.getForm().get("passwordRetypedTextField"), "change");

        tester.clickLink("form:passwordForm:submitButton3", true);
        tester.assertErrorMessages(formTester.getForm().getString("form.newPassword.Required"));
    }

    @Test
    void testSubmitNoRetypedPassword() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        FormTester formTester = tester.newFormTester("form:passwordForm");
        formTester.getForm().get("currentPasswordTextField").setDefaultModelObject("currentPassword");
        formTester.getForm().get("passwordTextField").setDefaultModelObject("newPassword");
        formTester.getForm().get("passwordRetypedTextField").setDefaultModelObject("");

        tester.clickLink("form:passwordForm:submitButton3");
        tester.assertErrorMessages(formTester.getForm().getString("form.newPasswordConfirmation.Required"));
    }

    @Override
    protected void setupTest() {
    }
}
