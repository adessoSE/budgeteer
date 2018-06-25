package org.wickedsource.budgeteer.web.pages.person.details.component;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsModel;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsPanel;

public class PersonHighlightsPanelTest extends AbstractWebTestTemplate {

	@Test
	void render() {
		WicketTester tester = getTester();
		PersonHighlightsModel model = new PersonHighlightsModel(1L);
		PersonHighlightsPanel panel = new PersonHighlightsPanel("panel", model);
		tester.startComponentInPage(panel);

		tester.assertContains("Tom Hombergs");
	}

	@Override
	protected void setupTest() {

	}
}
