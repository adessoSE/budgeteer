package org.wickedsource.budgeteer.web.pages.user.forgotpassword;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.MailNotFoundException;
import org.wickedsource.budgeteer.service.user.MailNotVerifiedException;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

@Mount("/forgotpassword")
public class ForgotPasswordPage extends DialogPage {

  @SpringBean private UserService service;

  public ForgotPasswordPage() {
    Injector.get().inject(this);
    Form<ForgotPasswordData> form =
        new Form<ForgotPasswordData>("forgotPasswordForm", Model.of(new ForgotPasswordData())) {
          @Override
          protected void onSubmit() {
            try {
              service.resetPassword(getModelObject().getMail());
              success(getString("message.success"));
            } catch (MailNotFoundException e) {
              error(getString("message.mailNotFound"));
            } catch (MailNotVerifiedException e) {
              error(getString("message.mailNotVerified"));
            }
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(
        new EmailTextField(
                "mail",
                of(form.getModel(), ForgotPasswordData::getMail, ForgotPasswordData::setMail))
            .setRequired(true));
    form.add(new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class));
  }
}
