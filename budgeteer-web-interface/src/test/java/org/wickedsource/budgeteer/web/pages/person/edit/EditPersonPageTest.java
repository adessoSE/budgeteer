package org.wickedsource.budgeteer.web.pages.person.edit;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.common.old.MoneyUtil;
import java.util.ArrayList;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.person.PersonWithRates;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;

class EditPersonPageTest extends AbstractWebTestTemplate {

  @BeforeEach
  void setUpMocks() {
    when(personServiceMock.loadPersonWithRates(anyLong()))
        .thenReturn(new PersonWithRates(123L, "name", "key", MoneyUtil.ZERO, new ArrayList<>()));
  }

  @Test
  void testRender() {
    WicketTester tester = getTester();
    EditPersonPage page =
        new EditPersonPage(EditPersonPage.createParameters(1), PeopleOverviewPage.class, null);
    tester.startPage(page);
    tester.assertRenderedPage(EditPersonPage.class);
  }

  @Test
  void testRedirectOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditPersonPage.class, new PageParameters());
    tester.assertRenderedPage(PeopleOverviewPage.class);
  }

  @Override
  protected void setupTest() {}
}
