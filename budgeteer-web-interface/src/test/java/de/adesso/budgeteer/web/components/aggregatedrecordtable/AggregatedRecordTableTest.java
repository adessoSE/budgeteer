package de.adesso.budgeteer.web.components.aggregatedrecordtable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.person.weekreport.table.PersonWeeklyAggregatedRecordsModel;

public class AggregatedRecordTableTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        WicketTester tester = getTester();
        PersonWeeklyAggregatedRecordsModel model = new PersonWeeklyAggregatedRecordsModel(1L);
        AggregatedRecordTable table = new AggregatedRecordTable("table", model);
        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {
    }
}
