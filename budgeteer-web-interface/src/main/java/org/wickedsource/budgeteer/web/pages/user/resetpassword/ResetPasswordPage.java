package org.wickedsource.budgeteer.web.pages.user.resetpassword;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import de.adesso.budgeteer.core.user.ExpiredForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.InvalidForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.port.in.ChangeForgottenPasswordUseCase;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("resetpassword")
public class ResetPasswordPage extends DialogPage {

  @SpringBean private ChangeForgottenPasswordUseCase changeForgottenPasswordUseCase;

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
        new Form<ResetPasswordData>("resetPasswordForm", model(from(new ResetPasswordData()))) {
          @Override
          protected void onSubmit() {
            if (resetToken != null) {
              if (!getModelObject()
                  .getNewPassword()
                  .equals(getModelObject().getNewPasswordConfirmation())) {
                error(getString("message.wrongPasswordConfirmation"));
                return;
              }
              try {
                changeForgottenPasswordUseCase.changeForgottenPassword(
                    new ChangeForgottenPasswordUseCase.ChangeForgottenPasswordCommand(
                        resetToken, getModelObject().getNewPassword()));
              } catch (InvalidForgottenPasswordTokenException e) {
                error(getString("message.tokenInvalid"));
              } catch (ExpiredForgottenPasswordTokenException e) {
                error(getString("message.tokenExpired"));
              }
              success(getString("message.success"));
            }
          }
        };

    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new PasswordTextField("newPassword", model(from(form.getModel()).getNewPassword()))
            .setRequired(true));
    form.add(
        new PasswordTextField(
                "newPasswordConfirmation",
                model(from(form.getModel()).getNewPasswordConfirmation()))
            .setRequired(true));
    form.add(new Button("submitButton"));
    form.add(new BookmarkablePageLink("backlink", DashboardPage.class));
  }
}
