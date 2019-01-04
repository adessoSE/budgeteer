package de.adesso.budgeteer.web.pages.dashboard;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class DashboardPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
    }

    @Override
    protected void setupTest() {
    }
}
