package org.wickedsource.budgeteer.web.pages.user.login;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class LoginPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(LoginPage.class);
        tester.assertRenderedPage(LoginPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
