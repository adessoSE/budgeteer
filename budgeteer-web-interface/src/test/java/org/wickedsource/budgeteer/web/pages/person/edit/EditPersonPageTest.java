package org.wickedsource.budgeteer.web.pages.person.edit;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;

public class EditPersonPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        EditPersonPage page = new EditPersonPage(null, PeopleOverviewPage.class, null);
        tester.startPage(page);
        tester.assertRenderedPage(EditPersonPage.class);
    }

}
