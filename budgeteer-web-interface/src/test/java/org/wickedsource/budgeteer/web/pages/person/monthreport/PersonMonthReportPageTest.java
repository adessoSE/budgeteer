package org.wickedsource.budgeteer.web.pages.person.monthreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.Date;

import static org.mockito.Mockito.when;

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
