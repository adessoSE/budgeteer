package org.wickedsource.budgeteer.web.pages.user.forgotpassword;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class ForgotPasswordPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(ForgotPasswordPage.class);
        tester.assertRenderedPage(ForgotPasswordPage.class);
    }

    @Override
    protected void setupTest() {

    }
}