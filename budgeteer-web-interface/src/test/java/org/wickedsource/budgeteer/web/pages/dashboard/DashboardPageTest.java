package org.wickedsource.budgeteer.web.pages.dashboard;

import de.adesso.budgeteer.core.project.domain.Project;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.when;

class DashboardPageTest extends AbstractWebTestTemplate {


  @Test
  void testRender() {
    var projectId = 1L;
    WicketTester tester = getTester();
    tester.startPage(DashboardPage.class);
    tester.assertRenderedPage(DashboardPage.class);
  }

  @Override
  protected void setupTest() {}
}
