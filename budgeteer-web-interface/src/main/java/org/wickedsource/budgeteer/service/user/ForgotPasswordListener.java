package org.wickedsource.budgeteer.service.user;

import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.MailNotFoundException;
import de.adesso.budgeteer.core.user.MailNotVerifiedException;
import de.adesso.budgeteer.core.user.port.in.ResetPasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForgotPasswordListener implements ApplicationListener<OnForgotPasswordEvent> {

  private final ResetPasswordUseCase resetPasswordUseCase;

  /**
   * Sends a mail with a link to reset the password as soon as a user requests a new one via the
   * corresponding page. A random token is generated via the UUID, see:
   * https://www.baeldung.com/java-uuid
   *
   * @param event triggers the corresponding event
   */
  @Override
  public void onApplicationEvent(OnForgotPasswordEvent event) {
    try {
      resetPasswordUseCase.resetPassword(event.getUserEntity().getPassword());
    } catch (MailNotFoundException | MailNotEnabledException | MailNotVerifiedException e) {
      /* Do nothing */
    }
  }
}
