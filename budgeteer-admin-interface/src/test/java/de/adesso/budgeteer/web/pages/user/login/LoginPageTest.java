package de.adesso.budgeteer.web.pages.user.login;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
