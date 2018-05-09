package org.wickedsource.budgeteer.web.pages.person.edit;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.person.PersonWithRates;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditPersonPageTest extends AbstractWebTestTemplate {

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUpMocks(){
        when(personService.loadPersonWithRates(anyLong())).thenReturn(new PersonWithRates(123L, "name", "key", new ArrayList<PersonRate>()));

    }

    @Test
    void testRender() {
        WicketTester tester = getTester();
        EditPersonPage page = new EditPersonPage(null, PeopleOverviewPage.class, null);
        tester.startPage(page);
        tester.assertRenderedPage(EditPersonPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
