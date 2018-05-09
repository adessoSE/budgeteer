package org.wickedsource.budgeteer.web.pages.person.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.overview.table.PeopleModel;
import org.wickedsource.budgeteer.web.pages.person.overview.table.PeopleOverviewTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class PeopleOverviewTableTest extends AbstractWebTestTemplate {

    @Autowired
    private PersonService service;

    @Test
    void testRender() {
        WicketTester tester = getTester();
        when(service.loadPeopleBaseData(1L)).thenReturn(createPersons());
        PeopleModel model = new PeopleModel(1L);
        PeopleOverviewTable table = new PeopleOverviewTable("table", model);
        tester.startComponentInPage(table);
    }

    private List<PersonBaseData> createPersons() {
        List<PersonBaseData> list = new ArrayList<PersonBaseData>();
        for (int i = 0; i < 20; i++) {
            PersonBaseData person = new PersonBaseData();
            person.setId(1L);
            person.setAverageDailyRate(MoneyUtil.createMoney(1250.54));
            person.setLastBooked(new Date());
            person.setName("Martha Pfahl");
            list.add(person);
        }
        return list;
    }

    @Override
    protected void setupTest() {

    }
}
