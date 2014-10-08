package org.wickedsource.budgeteer.web.usecase.dashboard;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;

public class DashboardPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
        assertLink("peopleLink1", PeopleOverviewPage.class);
        assertLink("peopleLink2", PeopleOverviewPage.class);
    }
}
