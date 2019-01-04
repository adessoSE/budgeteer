package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class ProjectAdministrationPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    @Override
    protected void setupTest() {

    }
}