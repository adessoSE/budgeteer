package org.wickedsource.budgeteer.web.usecase.people;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;

public class PeopleOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(PeopleOverviewPage.class);
        tester.assertRenderedPage(PeopleOverviewPage.class);
    }
}
