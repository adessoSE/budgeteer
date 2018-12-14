package de.adesso.budgeteer.web.pages.dashboard;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

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
