package org.wickedsource.budgeteer.web.usecase.people.component;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.Person;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class PeopleOverviewTableTest extends AbstractWebTestTemplate {

    @Autowired
    private PeopleService service;

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        when(service.getPeople(1l)).thenReturn(createPersons());
        PeopleModel model = new PeopleModel(1l);
        PeopleOverviewTable table = new PeopleOverviewTable("table", model);
        tester.startComponentInPage(table);
    }

    private List<Person> createPersons() {
        List<Person> list = new ArrayList<Person>();
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setId(1);
            person.setAverageDailyRate(1250.54);
            person.setLastBooked(new Date());
            person.setName("Martha Pfahl");
            list.add(person);
        }
        return list;
    }
}
