package org.wickedsource.budgeteer.web.pages.user.resettoken;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

class ResetTokenPageTest extends AbstractWebTestTemplate {

  @Test
  void test() {
    WicketTester tester = getTester();
    PageParameters pageParameters = new PageParameters();
    pageParameters.add("userId", 1L);
    pageParameters.add("valid", 0);
    ResetTokenPage resetTokenPage = new ResetTokenPage(pageParameters);
    tester.startPage(resetTokenPage);
    tester.assertRenderedPage(ResetTokenPage.class);
  }

  @Override
  protected void setupTest() {}
}
