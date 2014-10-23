package org.wickedsource.budgeteer.web.components.aggregatedrecordtable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.record.AggregatedWorkingRecord;
import org.wickedsource.budgeteer.service.record.WorkingRecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.people.weekreport.table.PersonWeeklyAggregatedRecordsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;

public class AggregatedRecordTableTest extends AbstractWebTestTemplate {

    @Autowired
    private WorkingRecordService service;

    private Random random = new Random();

    @Test
    public void render() {
        WicketTester tester = getTester();
        when(service.getWeeklyAggregationForPerson(1l)).thenReturn(getWeeklyAggregationForPerson(1l));
        PersonWeeklyAggregatedRecordsModel model = new PersonWeeklyAggregatedRecordsModel(1l);
        AggregatedRecordTable table = new AggregatedRecordTable("table", model);
        tester.startComponentInPage(table);
    }

    private List<AggregatedWorkingRecord> getWeeklyAggregationForPerson(long personId) {
        List<AggregatedWorkingRecord> list = new ArrayList<AggregatedWorkingRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedWorkingRecord record = new AggregatedWorkingRecord();
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
}
