package org.wickedsource.budgeteer.web.pages.people.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonBaseData;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.people.overview.table.PeopleModel;
import org.wickedsource.budgeteer.web.pages.people.overview.table.PeopleOverviewTable;

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
        when(service.loadPeopleBaseData(1l)).thenReturn(createPersons());
        PeopleModel model = new PeopleModel(1l);
        PeopleOverviewTable table = new PeopleOverviewTable("table", model);
        tester.startComponentInPage(table);
    }

    private List<PersonBaseData> createPersons() {
        List<PersonBaseData> list = new ArrayList<PersonBaseData>();
        for (int i = 0; i < 20; i++) {
            PersonBaseData person = new PersonBaseData();
            person.setId(1);
            person.setAverageDailyRate(MoneyUtil.createMoney(1250.54));
            person.setLastBooked(new Date());
            person.setName("Martha Pfahl");
            list.add(person);
        }
        return list;
    }
}