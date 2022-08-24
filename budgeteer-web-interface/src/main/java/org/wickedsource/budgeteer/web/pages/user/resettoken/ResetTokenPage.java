package org.wickedsource.budgeteer.web.pages.user.resettoken;

import de.adesso.budgeteer.core.user.EmailAlreadyVerifiedException;
import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.port.in.GetUserWithEmailUseCase;
import de.adesso.budgeteer.core.user.port.in.ResendVerificationTokenUseCase;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.TokenStatus;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("resettoken")
public class ResetTokenPage extends DialogPage {
  @SpringBean private GetUserWithEmailUseCase getUserWithEmailUseCase;
  @SpringBean private ResendVerificationTokenUseCase resendVerificationTokenUseCase;

  public ResetTokenPage() {
    addComponents(new PageParameters());
  }

  public ResetTokenPage(PageParameters pageParameters) {
    handleStatusCode(pageParameters);
    addComponents(pageParameters);
  }

  private void addComponents(PageParameters backlinkParameters) {
    Injector.get().inject(this);
    var userWithEmail =
        getUserWithEmailUseCase.getUserWithEmail(backlinkParameters.get("userId").toLong());
    var resetTokenData = new ResetTokenModel(userWithEmail.getId(), userWithEmail.getEmail());
    var form =
        new Form<>("resetTokenForm", Model.of(resetTokenData)) {
          @Override
          protected void onSubmit() {
            try {
              resendVerificationTokenUseCase.resendVerificationToken(getModelObject().getUserId());
            } catch (EmailAlreadyVerifiedException e) {
              error(getString("message.alreadyEnabled"));
            } catch (MailNotEnabledException e) {
              // TODO: Add message when mail isn't enabled
            }
            success(getString("message.mailSent"));
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(new Label("mail", LambdaModel.of(resetTokenData::getMail)));
    form.add(new Button("submitButton"));
    form.add(new BookmarkablePageLink<>("backlink", DashboardPage.class));
  }

  private void handleStatusCode(PageParameters pageParameters) {
    var valid = pageParameters.get("valid");
    if (valid.isNull() || valid.isEmpty()) {
      return;
    }
    int result = valid.toInt();
    if (result == TokenStatus.INVALID.statusCode()) {
      error(getString("message.tokenInvalid"));
    } else if (result == TokenStatus.EXPIRED.statusCode()) {
      error(getString("message.tokenExpired"));
    }
  }
}
