package org.wickedsource.budgeteer.web.usecase.people.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonDetailData;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.Date;

import static org.mockito.Mockito.when;

public class PersonDetailsPageTest extends AbstractWebTestTemplate {

    @Autowired
    private PeopleService service;

    @Test
    public void render() {
        WicketTester tester = getTester();
        when(service.loadPersonDetailData(1l)).thenReturn(createPerson());
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1l);
        tester.startPage(PersonDetailsPage.class, parameters);
        tester.assertRenderedPage(PersonDetailsPage.class);
    }

    private PersonDetailData createPerson() {
        PersonDetailData data = new PersonDetailData();
        data.setAverageDailyRate(100.0);
        data.setName("Tom Hombergs");
        data.setBudgetBurned(100000.00);
        data.setFirstBookedDate(new Date());
        data.setHoursBooked(100.0);
        data.setLastBookedDate(new Date());
        return data;
    }
}
