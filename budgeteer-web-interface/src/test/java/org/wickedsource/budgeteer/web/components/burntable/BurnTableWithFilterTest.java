package org.wickedsource.budgeteer.web.components.burntable;

import org.wickedsource.budgeteer.web.pages.hours.HoursPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BurnTableWithFilterTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        BurnTableWithFilter table = new BurnTableWithFilter("panel", new WorkRecordFilter(1L), new HoursPage(), null);
        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {

    }
}
