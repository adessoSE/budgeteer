package de.adesso.budgeteer.web.pages.user.selectproject;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.user.login.LoginPage;

public class SelectProjectPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        SelectProjectPage page = new SelectProjectPage(LoginPage.class, new PageParameters());
        tester.startPage(page);
        tester.assertRenderedPage(SelectProjectPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
