package de.adesso.budgeteer.web.pages.person.details.component;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.person.details.highlights.PersonHighlightsModel;
import de.adesso.budgeteer.web.pages.person.details.highlights.PersonHighlightsPanel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
