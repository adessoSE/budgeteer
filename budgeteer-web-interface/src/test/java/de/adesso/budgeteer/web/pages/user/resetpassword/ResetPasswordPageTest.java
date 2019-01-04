package de.adesso.budgeteer.web.pages.user.resetpassword;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class ResetPasswordPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(ResetPasswordPage.class);
        tester.assertRenderedPage(ResetPasswordPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
