package org.wickedsource.budgeteer.web.pages.user.resettoken;

import static org.mockito.Mockito.when;

import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.in.GetUserWithEmailUseCase;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

class ResetTokenPageTest extends AbstractWebTestTemplate {

  @Autowired private GetUserWithEmailUseCase getUserWithEmailUseCase;

  @Test
  void test() {
    var userId = 1L;
    when(getUserWithEmailUseCase.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, "username", "mail"));
    WicketTester tester = getTester();
    PageParameters pageParameters = new PageParameters();
    pageParameters.add("userId", userId);
    pageParameters.add("valid", 0);
    ResetTokenPage resetTokenPage = new ResetTokenPage(pageParameters);
    tester.startPage(resetTokenPage);
    tester.assertRenderedPage(ResetTokenPage.class);
  }

  @Override
  protected void setupTest() {}
}
