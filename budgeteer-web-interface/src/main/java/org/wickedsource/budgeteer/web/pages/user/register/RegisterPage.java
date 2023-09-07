package org.wickedsource.budgeteer.web.pages.user.register;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.MailAlreadyInUseException;
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
    Injector.get().inject(this);
    Form<RegistrationData> form =
        new Form<RegistrationData>("registrationForm", Model.of(new RegistrationData())) {
          @Override
          protected void onSubmit() {
            if (!getModelObject()
                .getPassword()
                .equals(getModelObject().getPasswordConfirmation())) {
              error(getString("message.wrongPasswordConfirmation"));
              return;
            }
            try {
              service.registerUser(
                  getModelObject().getUsername(),
                  getModelObject().getMail(),
                  getModelObject().getPassword());
              setResponsePage(
                  LoginPage.class, new PageParameters().add("verificationsent", "true"));
            } catch (UsernameAlreadyInUseException e) {
              this.error(getString("message.duplicateUserName"));
            } catch (MailAlreadyInUseException e) {
              this.error(getString("message.duplicateMail"));
            }
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new RequiredTextField<String>(
            "username",
            of(form.getModel(), RegistrationData::getUsername, RegistrationData::setUsername)));
    form.add(
        new EmailTextField(
                "mail", of(form.getModel(), RegistrationData::getMail, RegistrationData::setMail))
            .setRequired(true));
    form.add(
        new PasswordTextField(
            "password",
            of(form.getModel(), RegistrationData::getPassword, RegistrationData::setPassword)));
    form.add(
        new PasswordTextField(
            "passwordConfirmation",
            of(
                form.getModel(),
                RegistrationData::getPasswordConfirmation,
                RegistrationData::setPasswordConfirmation)));
    form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
  }
}
