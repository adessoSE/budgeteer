package org.wickedsource.budgeteer.web.pages.person.monthreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PersonMonthReportPageTest extends AbstractWebTestTemplate{

    @Test
    void test() {
        WicketTester tester = getTester();
        tester.startPage(PersonMonthReportPage.class, PersonMonthReportPage.createParameters(1L));
        tester.assertRenderedPage(PersonMonthReportPage.class);
    }

    @Override
    protected void setupTest() {

    }
}