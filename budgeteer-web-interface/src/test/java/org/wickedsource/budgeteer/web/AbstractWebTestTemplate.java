package org.wickedsource.budgeteer.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.service.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public abstract class AbstractWebTestTemplate {

    @Autowired
    BudgeteerApplication application;

    private static WicketTester tester;

    public void login() {
        User user = new User();
        user.setId(1l);
        user.setName("username");
        BudgeteerSession.get().login(user);
    }

    protected WicketTester getTester() {
        if (tester == null) {
            tester = new WicketTester(application);
            login();
        }
        return tester;
    }

}
