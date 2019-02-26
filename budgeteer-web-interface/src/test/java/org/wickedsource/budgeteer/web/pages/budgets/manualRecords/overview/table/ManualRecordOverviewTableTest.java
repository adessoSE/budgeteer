package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview.table;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordModel;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ManualRecordOverviewTableTest extends AbstractWebTestTemplate {
    @Autowired
    private ManualRecordService service;

    private Random random = new Random();

    @Override
    protected void setupTest() {

    }

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadManualRecord(anyLong())).thenReturn(createTestRate());
        when(service.getManualRecords(anyLong())).thenReturn(createTestData());

        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);

        ManualRecordModel model = new ManualRecordModel(1L, service);
        ManualRecordOverviewTable table = new ManualRecordOverviewTable("table", model, parameters);

        tester.startComponentInPage(table);
    }

    private ManualRecord createTestRate() {
        ManualRecord record = new ManualRecord();

        record.setMoneyAmount(MoneyUtil.createMoneyFromCents(random.nextInt(10000)));
        record.setCreationDate(new Date());
        record.setDescription("test record");
        record.setBudgetId(1L);
        record.setBillingDate(new Date());
        record.setId(1L);

        return record;
    }

    private List<ManualRecord> createTestData() {
        List<ManualRecord> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ManualRecord record = new ManualRecord();

            record.setMoneyAmount(MoneyUtil.createMoneyFromCents(random.nextInt(10000)));
            record.setCreationDate(new Date());
            record.setDescription("test record " + i);
            record.setBudgetId(1L);
            record.setBillingDate(new Date());
            record.setId(i);

            list.add(record);
        }
        return list;
    }
}
