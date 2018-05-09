package org.wickedsource.budgeteer.web.pages.person.details.component;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsModel;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsPanel;

import java.util.Date;

import static org.mockito.Mockito.when;

public class PersonHighlightsPanelTest extends AbstractWebTestTemplate {

    @Autowired
    private PersonService service;

    @Test
    void render() {
        when(service.loadPersonDetailData(1L)).thenReturn(createDetailData());
        WicketTester tester = getTester();
        PersonHighlightsModel model = new PersonHighlightsModel(1L);
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

    @Override
    protected void setupTest() {

    }
}
