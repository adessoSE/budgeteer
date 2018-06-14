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

    @Test
    void testRender() {
        WicketTester tester = getTester();
        PeopleModel model = new PeopleModel(1L);
        PeopleOverviewTable table = new PeopleOverviewTable("table", model);
        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {

    }
}
