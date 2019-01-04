package de.adesso.budgeteer.web.pages.person.weekreport;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class PersonWeekReportPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(PersonWeekReportPage.class, PersonWeekReportPage.createParameters(1L));
        tester.assertRenderedPage(PersonWeekReportPage.class);
    }

    @Override
    protected void setupTest() {

    }
}