package org.wickedsource.budgeteer.web.component.burntable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.component.burntable.filter.FilterPanel;

public class FilterPanelTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        FilterPanel panel = new FilterPanel("panel", new RecordFilter());
        tester.startComponentInPage(panel);
    }
}
