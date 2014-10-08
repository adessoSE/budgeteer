package org.wickedsource.budgeteer.web.usecase.people.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class PersonDetailsPageTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1l);
        tester.startPage(PersonDetailsPage.class, parameters);
        tester.assertRenderedPage(PersonDetailsPage.class);
    }
}
