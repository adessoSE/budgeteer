package org.wickedsource.budgeteer.web.pages.person.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PersonHoursPageTest extends AbstractWebTestTemplate {

	@Test
	void render() {
		WicketTester tester = getTester();
		tester.startPage(PersonHoursPage.class, PersonHoursPage.createParameters(1L));
		tester.assertRenderedPage(PersonHoursPage.class);
	}

	@Override
	protected void setupTest() {}
}
