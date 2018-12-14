package de.adesso.budgeteer.web.components.burntable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.service.record.WorkRecordFilter;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.components.burntable.filter.FilterPanel;
import de.adesso.budgeteer.web.pages.hours.HoursPage;

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
