package org.wickedsource.budgeteer.web.people;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.dashboard.DashboardPage;

public class PeopleOverviewPageTest {

    @Test
    public void testRender() {
        WicketTester tester = new WicketTester();
        tester.startPage(PeopleOverviewPage.class);
        tester.assertRenderedPage(PeopleOverviewPage.class);
    }
}
