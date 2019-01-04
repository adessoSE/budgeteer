package de.adesso.budgeteer.web.pages.hours;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class HoursPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(HoursPage.class);
        tester.assertRenderedPage(HoursPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
