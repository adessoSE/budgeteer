package org.wickedsource.budgeteer.web.component.burntable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BurnTableWithFilterTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        BurnTableWithFilter table = new BurnTableWithFilter("panel", new RecordFilter());
        tester.startComponentInPage(table);
    }
}
