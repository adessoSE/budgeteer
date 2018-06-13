package org.wickedsource.budgeteer.web.pages.budgets.hours;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BudgetHoursPageTest extends AbstractWebTestTemplate {

	@Test
	void render() {
		WicketTester tester = getTester();
		tester.startPage(BudgetHoursPage.class, BudgetHoursPage.createParameters(1L));
		tester.assertRenderedPage(BudgetHoursPage.class);
	}

	@Override
	protected void setupTest() {}
}
