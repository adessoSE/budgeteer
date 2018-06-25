package org.wickedsource.budgeteer.web.pages.person.weekreport;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PersonWeekReportPageTest extends AbstractWebTestTemplate {

	@Test
	void test() {
		WicketTester tester = getTester();
		tester.startPage(PersonWeekReportPage.class, PersonWeekReportPage.createParameters(1L));
		tester.assertRenderedPage(PersonWeekReportPage.class);
	}

	@Override
	protected void setupTest() {

	}
}