package org.wickedsource.budgeteer.web.dashboard;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class DashboardPageTest {

    @Test
    public void testRender() {
        WicketTester tester = new WicketTester();
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
    }
}
