package org.wickedsource.budgeteer.web.pages.people.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PeopleOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(PeopleOverviewPage.class);
        tester.assertRenderedPage(PeopleOverviewPage.class);
    }
}
