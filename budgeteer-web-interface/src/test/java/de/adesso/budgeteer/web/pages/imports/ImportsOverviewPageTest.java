package de.adesso.budgeteer.web.pages.imports;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

public class ImportsOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(ImportsOverviewPage.class);
        tester.assertRenderedPage(ImportsOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
