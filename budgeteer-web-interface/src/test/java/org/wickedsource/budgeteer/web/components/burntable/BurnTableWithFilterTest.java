package org.wickedsource.budgeteer.web.components.burntable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class BurnTableWithFilterTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        BurnTableWithFilter table = new BurnTableWithFilter("panel", new WorkRecordFilter(1l));
        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {

    }
}
