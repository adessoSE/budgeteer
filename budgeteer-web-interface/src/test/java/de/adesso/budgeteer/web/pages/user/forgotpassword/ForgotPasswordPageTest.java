package de.adesso.budgeteer.web.pages.user.forgotpassword;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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