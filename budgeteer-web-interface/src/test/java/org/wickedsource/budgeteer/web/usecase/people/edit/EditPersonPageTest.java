package org.wickedsource.budgeteer.web.usecase.people.edit;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class EditPersonPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(EditPersonPage.class);
        tester.assertRenderedPage(EditPersonPage.class);
    }

}
