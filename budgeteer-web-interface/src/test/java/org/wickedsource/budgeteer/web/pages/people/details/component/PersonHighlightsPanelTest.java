package org.wickedsource.budgeteer.web.pages.people.details.component;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonDetailData;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.people.details.highlights.PersonHighlightsModel;
import org.wickedsource.budgeteer.web.pages.people.details.highlights.PersonHighlightsPanel;

import java.util.Date;

import static org.mockito.Mockito.when;

public class PersonHighlightsPanelTest extends AbstractWebTestTemplate {

    @Autowired
    private PeopleService service;

    @Test
    public void render() {
        when(service.loadPersonDetailData(1l)).thenReturn(createDetailData());
        WicketTester tester = getTester();
        PersonHighlightsModel model = new PersonHighlightsModel(1l);
        PersonHighlightsPanel panel = new PersonHighlightsPanel("panel", model);
        tester.startComponentInPage(panel);

        tester.assertContains("Tom Hombergs");
    }

    private PersonDetailData createDetailData() {
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
