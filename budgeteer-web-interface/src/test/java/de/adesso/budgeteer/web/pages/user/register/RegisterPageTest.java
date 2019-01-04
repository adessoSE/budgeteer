package de.adesso.budgeteer.web.pages.user.register;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class RegisterPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(RegisterPage.class);
        tester.assertRenderedPage(RegisterPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
