package org.wickedsource.budgeteer.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.wickedsource.budgeteer.service.user.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public abstract class AbstractWebTestTemplate {

    @Autowired
    BudgeteerApplication application;

    private static WicketTester tester;

    @Before
    public void setUp() {
        if (tester == null) {
            tester = new WicketTester(application);
            login();
        }
        setupTest();
    }

    /**
     * Subclasses can use this method to provide the configuration needed by
     * each test.
     */
    protected abstract void setupTest();

    public void login() {
        User user = new User();
        user.setId(1l);
        user.setName("username");
        BudgeteerSession.get().login(user);
    }

    protected WicketTester getTester() {
        return tester;
    }
}
