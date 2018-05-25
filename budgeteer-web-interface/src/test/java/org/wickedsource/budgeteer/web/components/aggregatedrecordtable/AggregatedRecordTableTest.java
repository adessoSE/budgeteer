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

    @Autowired
    private RecordService service;

    private Random random = new Random();

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.getWeeklyAggregationForPerson(1L)).thenReturn(getWeeklyAggregationForPerson(1L));
        PersonWeeklyAggregatedRecordsModel model = new PersonWeeklyAggregatedRecordsModel(1L);
        AggregatedRecordTable table = new AggregatedRecordTable("table", model);
        tester.startComponentInPage(table);
    }

    private List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
        List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedRecord record = new AggregatedRecord();
            record.setAggregationPeriodTitle("Week #" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodStart(new Date());
            record.setBudgetBurned(MoneyUtil.createMoneyFromCents(random.nextInt(8000)));
            record.setBudgetPlanned(MoneyUtil.createMoneyFromCents(random.nextInt(4000)));
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }

    @Override
    protected void setupTest() {

    }
}
