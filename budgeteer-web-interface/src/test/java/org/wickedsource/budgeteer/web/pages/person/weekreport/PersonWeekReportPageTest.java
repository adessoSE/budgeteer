package org.wickedsource.budgeteer.web.pages.person.weekreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.Date;

import static org.mockito.Mockito.when;

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
