package org.wickedsource.budgeteer.web.components.aggregatedrecordtable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.person.weekreport.table.PersonWeeklyAggregatedRecordsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;

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
