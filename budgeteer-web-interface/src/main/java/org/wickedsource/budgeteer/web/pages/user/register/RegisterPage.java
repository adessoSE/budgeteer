package org.wickedsource.budgeteer.web.pages.user.register;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

@Mount("/register")
public class RegisterPage extends DialogPage {

  @SpringBean private RegisterUseCase registerUseCase;

  public RegisterPage() {
    Injector.get().inject(this);
    var form =
        new Form<>("registrationForm", Model.of(new RegistrationData())) {
          @Override
          protected void onSubmit() {
            if (!getModelObject()
                .getPassword()
                .equals(getModelObject().getPasswordConfirmation())) {
              error(getString("message.wrongPasswordConfirmation"));
              return;
            }
            try {
              registerUseCase.register(
                  new RegisterUseCase.RegisterCommand(
                      getModelObject().getUsername(),
                      getModelObject().getMail(),
                      getModelObject().getPassword()));
              setResponsePage(
                  LoginPage.class, new PageParameters().add("verificationsent", "true"));
            } catch (UserException e) {
              if (e.getCauses().contains(UserException.UserErrors.MAIL_ALREADY_IN_USE)) {
                error(getString("message.duplicateMail"));
              }
              if (e.getCauses().contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE)) {
                this.error(getString("message.duplicateUserName"));
              }
            }
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new RequiredTextField<>(
            "username",
            LambdaModel.of(
                form.getModel(), RegistrationData::getUsername, RegistrationData::setUsername)));
    form.add(
        new EmailTextField(
                "mail",
                LambdaModel.of(
                    form.getModel(), RegistrationData::getMail, RegistrationData::setMail))
            .setRequired(true));
    form.add(
        new PasswordTextField(
            "password",
            LambdaModel.of(
                form.getModel(), RegistrationData::getPassword, RegistrationData::setPassword)));
    form.add(
        new PasswordTextField(
            "passwordConfirmation",
            LambdaModel.of(
                form.getModel(),
                RegistrationData::getPasswordConfirmation,
                RegistrationData::setPasswordConfirmation)));
    form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
  }
}
