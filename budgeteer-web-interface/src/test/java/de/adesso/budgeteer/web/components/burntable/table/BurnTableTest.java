package de.adesso.budgeteer.web.components.burntable.table;

import de.adesso.budgeteer.service.record.WorkRecordFilter;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.components.burntable.filter.FilteredRecordsModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BurnTableTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        BurnTable table = new BurnTable("table", new FilteredRecordsModel(new WorkRecordFilter(1L)));
        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {

    }
}
