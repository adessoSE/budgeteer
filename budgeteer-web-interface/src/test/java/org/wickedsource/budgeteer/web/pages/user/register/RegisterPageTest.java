package org.wickedsource.budgeteer.web.pages.user.register;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

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
