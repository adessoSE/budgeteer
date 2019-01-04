package de.adesso.budgeteer.web.pages.person.hours;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class PersonHoursPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(PersonHoursPage.class, PersonHoursPage.createParameters(1L));
        tester.assertRenderedPage(PersonHoursPage.class);
    }
    @Override
    protected void setupTest() {

    }
}