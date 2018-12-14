package de.adesso.budgeteer.web.pages.user.resetpassword;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

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
