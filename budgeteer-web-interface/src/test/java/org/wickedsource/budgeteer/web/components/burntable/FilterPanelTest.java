package org.wickedsource.budgeteer.web.components.burntable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilterPanel;
import org.wickedsource.budgeteer.web.pages.hours.HoursPage;

public class FilterPanelTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        FilterPanel panel = new FilterPanel("panel", new WorkRecordFilter(1L), new HoursPage(), null);
        tester.startComponentInPage(panel);
    }

    @Override
    protected void setupTest() {

    }
}
