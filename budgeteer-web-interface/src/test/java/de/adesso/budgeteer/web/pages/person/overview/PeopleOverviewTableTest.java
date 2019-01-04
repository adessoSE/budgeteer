package de.adesso.budgeteer.web.pages.person.overview;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.person.overview.table.PeopleModel;
import de.adesso.budgeteer.web.pages.person.overview.table.PeopleOverviewTable;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
