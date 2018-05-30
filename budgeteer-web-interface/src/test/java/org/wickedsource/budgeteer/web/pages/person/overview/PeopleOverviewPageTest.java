package org.wickedsource.budgeteer.web.pages.person.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PeopleOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        tester.startPage(PeopleOverviewPage.class);
        tester.assertRenderedPage(PeopleOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
