package org.wickedsource.budgeteer.web.dashboard;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.web.base.BudgeteerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public class DashboardPageTest {

    @Autowired
    BudgeteerApplication application;

    @Test
    public void testRender() {
        WicketTester tester = new WicketTester(application);
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
    }
}
