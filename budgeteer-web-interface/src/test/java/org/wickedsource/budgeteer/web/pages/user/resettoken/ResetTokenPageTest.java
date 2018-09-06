package org.wickedsource.budgeteer.web.pages.user.resettoken;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class ResetTokenPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(ResetTokenPage.class);
        tester.assertRenderedPage(ResetTokenPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
