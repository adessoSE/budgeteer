package org.wickedsource.budgeteer.web.pages.people.monthreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonDetailData;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.Date;

import static org.mockito.Mockito.when;

public class PersonMonthReportPageTest extends AbstractWebTestTemplate{

    @Autowired
    private PeopleService service;

    @Test
    public void test() {
        WicketTester tester = getTester();
        when(service.loadPersonDetailData(1l)).thenReturn(createPerson());
        tester.startPage(PersonMonthReportPage.class, PersonMonthReportPage.createParameters(1l));
        tester.assertRenderedPage(PersonMonthReportPage.class);
    }

    private PersonDetailData createPerson() {
        PersonDetailData data = new PersonDetailData();
        data.setAverageDailyRate(MoneyUtil.createMoney(100.0));
        data.setName("Tom Hombergs");
        data.setBudgetBurned(MoneyUtil.createMoney(100000.00));
        data.setFirstBookedDate(new Date());
        data.setHoursBooked(100.0);
        data.setLastBookedDate(new Date());
        return data;
    }
}
