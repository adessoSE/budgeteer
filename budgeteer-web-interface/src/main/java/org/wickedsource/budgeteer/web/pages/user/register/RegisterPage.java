package org.wickedsource.budgeteer.web.pages.user.register;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.service.user.UsernameAlreadyInUseException;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

@Mount("/register")
public class RegisterPage extends DialogPage {

  @SpringBean private UserService service;

  public RegisterPage() {
    var form =
        new Form<>("registrationForm", Model.of(new RegistrationData())) {
          @Override
          protected void onSubmit() {
            try {
              service.registerUser(getModelObject().getUsername(), getModelObject().getPassword());
              setResponsePage(LoginPage.class);
            } catch (UsernameAlreadyInUseException e) {
              this.error(getString("message.duplicateUserName"));
            }
          }
        };
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new RequiredTextField<>(
            "username",
            of(form.getModel(), RegistrationData::getUsername, RegistrationData::setUsername)));
    var passwordField =
        new PasswordTextField(
            "password",
            of(form.getModel(), RegistrationData::getPassword, RegistrationData::setPassword));
    var passwordConfirmationField =
        new PasswordTextField(
            "passwordConfirmation",
            of(
                form.getModel(),
                RegistrationData::getPasswordConfirmation,
                RegistrationData::setPasswordConfirmation));
    form.add(passwordField);
    form.add(passwordConfirmationField);
    form.add(new EqualPasswordInputValidator(passwordField, passwordConfirmationField));
    form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
    add(form);
  }
}
