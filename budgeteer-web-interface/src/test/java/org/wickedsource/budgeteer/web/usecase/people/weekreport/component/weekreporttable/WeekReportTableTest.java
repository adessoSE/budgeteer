package org.wickedsource.budgeteer.web.usecase.people.weekreport.component.weekreporttable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.hours.AggregatedRecord;
import org.wickedsource.budgeteer.service.hours.AggregationService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;

public class WeekReportTableTest extends AbstractWebTestTemplate {

    @Autowired
    private AggregationService service;

    private Random random = new Random();

    @Test
    public void render() {
        WicketTester tester = getTester();
        when(service.getWeeklyAggregationForPerson(1l)).thenReturn(getWeeklyAggregationForPerson(1l));
        PersonWeekReportModel model = new PersonWeekReportModel(1l);
        PersonWeekReportTable table = new PersonWeekReportTable("table", model);
        tester.startComponentInPage(table);
    }

    public List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
        List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedRecord record = new AggregatedRecord();
            record.setAggregationPeriodTitle("Week #" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodStart(new Date());
            record.setBudgetBurned(random.nextDouble());
            record.setBudgetPlanned(random.nextDouble());
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }
}
