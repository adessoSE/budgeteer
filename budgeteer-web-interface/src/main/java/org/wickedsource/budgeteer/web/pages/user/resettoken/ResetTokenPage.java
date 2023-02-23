package org.wickedsource.budgeteer.web.pages.user.resettoken;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import de.adesso.budgeteer.core.user.EmailAlreadyVerifiedException;
import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.port.in.GetUserWithEmailUseCase;
import de.adesso.budgeteer.core.user.port.in.ResendVerificationTokenUseCase;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.*;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("resettoken")
public class ResetTokenPage extends DialogPage {

  @SpringBean private ResendVerificationTokenUseCase resendVerificationTokenUseCase;
  @SpringBean private GetUserWithEmailUseCase getUserWithEmailUseCase;

  public ResetTokenPage() {
    addComponents(new PageParameters());
  }

  public ResetTokenPage(PageParameters pageParameters) {
    handleStatusCode(pageParameters);
    addComponents(pageParameters);
  }

  private void addComponents(PageParameters backlinkParameters) {
    Injector.get().inject(this);
    ResetTokenData resetTokenData = new ResetTokenData();
    resetTokenData.setMail(
        getUserWithEmailUseCase
            .getUserWithEmail(Long.parseLong(backlinkParameters.get("userId").toString()))
            .getEmail());
    Form<ResetTokenData> form =
        new Form<ResetTokenData>("resetTokenForm", model(from(resetTokenData))) {
          @Override
          protected void onSubmit() {
            try {
              resendVerificationTokenUseCase.resendVerificationToken(
                  backlinkParameters.get("userId").toLong());
            } catch (EmailAlreadyVerifiedException e) {
              error(getString("message.alreadyEnabled"));
            } catch (MailNotEnabledException e) {
              error(getString("message.mailNotFound"));
            }
            success(getString("message.mailSent"));
          }
        };
    add(form);
    form.add(new CustomFeedbackPanel("feedback"));
    form.add(new EmailTextField("mail", model(from(form.getModel()).getMail())).setRequired(true));
    form.add(new Button("submitButton"));
    form.add(new BookmarkablePageLink("backlink", DashboardPage.class));
  }

  private void handleStatusCode(PageParameters pageParameters) {
    if (!pageParameters.get("valid").isNull() && !pageParameters.get("valid").isEmpty()) {
      int result = pageParameters.get("valid").toInt();

      if (result == TokenStatus.INVALID.statusCode()) {
        error(getString("message.tokenInvalid"));
      } else if (result == TokenStatus.EXPIRED.statusCode()) {
        error(getString("message.tokenExpired"));
      }
    }
  }
}
