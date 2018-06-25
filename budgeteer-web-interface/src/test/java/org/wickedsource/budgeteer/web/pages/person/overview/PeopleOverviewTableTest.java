package org.wickedsource.budgeteer.web.pages.person.overview;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.overview.table.PeopleModel;
import org.wickedsource.budgeteer.web.pages.person.overview.table.PeopleOverviewTable;

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
