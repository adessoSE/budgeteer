package org.wickedsource.budgeteer.web.pages.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class HoursPageTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        tester.startPage(HoursPage.class);
        tester.assertRenderedPage(HoursPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
