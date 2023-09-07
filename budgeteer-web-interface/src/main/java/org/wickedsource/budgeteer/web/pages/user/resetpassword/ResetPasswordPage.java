package org.wickedsource.budgeteer.web.pages.user.resetpassword;

import static org.apache.wicket.model.LambdaModel.of;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.*;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("resetpassword")
public class ResetPasswordPage extends DialogPage {

  @SpringBean private UserService userService;

  public ResetPasswordPage() {
    addComponents(new PageParameters());
    error(getString("message.error"));
  }

  public ResetPasswordPage(PageParameters pageParameters) {
    addComponents(pageParameters);
  }

  private void addComponents(PageParameters pageParameters) {
    String resetToken = pageParameters.get("resettoken").toString();

    Injector.get().inject(this);
    Form<ResetPasswordData> form =
        new Form<ResetPasswordData>("resetPasswordForm", Model.of(new ResetPasswordData())) {
          @Override
          protected void onSubmit() {
            if (resetToken != null) {
              int result = userService.validateForgotPasswordToken(resetToken);

              if (result == TokenStatus.VALID.statusCode()) {
                if (!getModelObject()
                    .getNewPassword()
                    .equals(getModelObject().getNewPasswordConfirmation())) {
                  error(getString("message.wrongPasswordConfirmation"));
                  return;
                } else {
                  UserEntity userEntity = userService.getUserByForgotPasswordToken(resetToken);
                  EditUserData editUserData = userService.loadUserToEdit(userEntity.getId());
                  editUserData.setPassword(getModelObject().getNewPassword());

                  try {
                    userService.saveUser(editUserData, true);
                  } catch (UsernameAlreadyInUseException | MailAlreadyInUseException e) {
                    error(getString("message.error"));
                  }

                  userService.deleteForgotPasswordToken(resetToken);
                  success(getString("message.success"));
                }
              } else if (result == TokenStatus.INVALID.statusCode()) {
                error(getString("message.tokenInvalid"));
              } else if (result == TokenStatus.EXPIRED.statusCode()) {
                error(getString("message.tokenExpired"));
              }
            }
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new PasswordTextField(
                "newPassword",
                of(
                    form.getModel(),
                    ResetPasswordData::getNewPassword,
                    ResetPasswordData::setNewPassword))
            .setRequired(true));
    form.add(
        new PasswordTextField(
                "newPasswordConfirmation",
                of(
                    form.getModel(),
                    ResetPasswordData::getNewPasswordConfirmation,
                    ResetPasswordData::setNewPasswordConfirmation))
            .setRequired(true));
    form.add(new Button("submitButton"));
    form.add(new BookmarkablePageLink("backlink", DashboardPage.class));
  }
}
