package org.wickedsource.budgeteer.web.pages.user.edituser;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;

@SpringBootTest
class EditUserPageTest extends AbstractWebTestTemplate {
  @Test
  void test() {
    WicketTester tester = getTester();
    EditUserPage page =
        new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
    tester.startPage(page);
    tester.assertRenderedPage(EditUserPage.class);
  }

  @Override
  protected void setupTest() {}
}
