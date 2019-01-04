package de.adesso.budgeteer.web.pages.person.edit;

import de.adesso.budgeteer.service.person.PersonService;
import de.adesso.budgeteer.service.person.PersonWithRates;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditPersonPageTest extends AbstractWebTestTemplate {

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUpMocks(){
        when(personService.loadPersonWithRates(anyLong())).thenReturn(new PersonWithRates(123L, "name", "key", new ArrayList<>()));

    }

    @Test
    void testRender() {
        WicketTester tester = getTester();
        EditPersonPage page = new EditPersonPage(EditPersonPage.createParameters(1), PeopleOverviewPage.class, null);
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
    protected void setupTest() {

    }
}
