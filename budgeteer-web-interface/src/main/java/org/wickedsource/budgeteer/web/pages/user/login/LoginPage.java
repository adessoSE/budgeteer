package org.wickedsource.budgeteer.web.pages.user.login;

import de.adesso.budgeteer.core.user.ExpiredVerificationTokenException;
import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.InvalidVerificationTokenException;
import de.adesso.budgeteer.core.user.port.in.LoginUseCase;
import de.adesso.budgeteer.core.user.port.in.VerifyEmailUseCase;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.TokenStatus;
import org.wickedsource.budgeteer.service.user.UserModelMapper;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.forgotpassword.ForgotPasswordPage;
import org.wickedsource.budgeteer.web.pages.user.register.RegisterPage;
import org.wickedsource.budgeteer.web.pages.user.resettoken.ResetTokenPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

@Mount("/login")
public class LoginPage extends DialogPage {

  @SpringBean private VerifyEmailUseCase verifyEmailUseCase;
  @SpringBean private LoginUseCase loginUseCase;
  @SpringBean private UserModelMapper userModelMapper;

  public LoginPage() {
    build();
  }

  public LoginPage(PageParameters pageParameters) {
    // The token needed to verify the mail address is taken from the parameters and handled.
    String verificationToken = pageParameters.get("verificationtoken").toString();
    String verificationSent = pageParameters.get("verificationsent").toString();

    if (verificationToken != null) {
      try {
        verifyEmailUseCase.verifyEmail(verificationToken);
      } catch (InvalidVerificationTokenException e) {
        setResponsePage(
            ResetTokenPage.class,
            new PageParameters().add("valid", TokenStatus.INVALID.statusCode()));
        return;
      } catch (ExpiredVerificationTokenException e) {
        setResponsePage(
            ResetTokenPage.class,
            new PageParameters().add("valid", TokenStatus.EXPIRED.statusCode()));
        return;
      }
      success(getString("message.tokenValid"));
    } else if (verificationSent.equals("true")) {
      success(getString("message.mailSent"));
    }

    build();
  }

  private void build() {
    var form =
        new Form<>("loginForm", Model.of(new LoginCredentials())) {
          @Override
          protected void onSubmit() {
            try {
              var userModel =
                  userModelMapper.map(
                      loginUseCase.login(
                          getModelObject().getUsername(), getModelObject().getPassword()));
              BudgeteerSession.get().login(userModel);
              SelectProjectPage nextPage =
                  new SelectProjectPage(LoginPage.class, getPageParameters());
              setResponsePage(nextPage);
            } catch (InvalidLoginCredentialsException e) {
              error(getString("message.invalidLogin"));
            }
          }
        };
    add(form);

    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new RequiredTextField<>(
            "username",
            LambdaModel.of(
                form.getModel(), LoginCredentials::getUsername, LoginCredentials::setUsername)));
    form.add(
        new PasswordTextField(
            "password",
            LambdaModel.of(
                form.getModel(), LoginCredentials::getPassword, LoginCredentials::setPassword)));
    form.add(new BookmarkablePageLink<RegisterPage>("registerLink", RegisterPage.class));
    form.add(
        new BookmarkablePageLink<ForgotPasswordPage>(
            "forgotPasswordLink", ForgotPasswordPage.class));
  }
}
