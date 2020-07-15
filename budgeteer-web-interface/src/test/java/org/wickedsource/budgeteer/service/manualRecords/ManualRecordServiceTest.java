package org.wickedsource.budgeteer.service.manualRecords;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.wickedsource.budgeteer.IntegrationTestConfiguration;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.ServiceIntegrationTestTemplate;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
public class ManualRecordServiceTest extends ServiceIntegrationTestTemplate {
    @Autowired
    private ManualRecordService manualRecordService;

    @Test
    @DatabaseSetup("manualRecords.xml")
    @DatabaseTearDown(value = "manualRecords.xml", type = DatabaseOperation.DELETE_ALL)
    void testGetManualRecords() {
        List<ManualRecord> records = manualRecordService.getManualRecords(1L);
        Assertions.assertEquals(2, records.size());

        Calendar calendar = new GregorianCalendar(2016, 0, 3);
        Date date1 = calendar.getTime();
        calendar.set(Calendar.MONTH, 1);
        Date date2 = calendar.getTime();

        Assertions.assertEquals("manual 1", records.get(0).getDescription());
        Assertions.assertEquals(date2, records.get(0).getBillingDate());
        Assertions.assertEquals(100, records.get(0).getId());
        Assertions.assertEquals(date2, records.get(0).getCreationDate());
        Assertions.assertEquals(1, records.get(0).getBudgetId());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(800), records.get(0).getMoneyAmount());

        Assertions.assertEquals("manual 2", records.get(1).getDescription());
        Assertions.assertEquals(date1, records.get(1).getBillingDate());
        Assertions.assertEquals(200, records.get(1).getId());
        Assertions.assertEquals(date1, records.get(1).getCreationDate());
        Assertions.assertEquals(1, records.get(1).getBudgetId());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(400), records.get(1).getMoneyAmount());
    }

    @Test
    @DatabaseSetup("manualRecords.xml")
    @DatabaseTearDown(value = "manualRecords.xml", type = DatabaseOperation.DELETE_ALL)
    void testLoadManualRecord() {
        ManualRecord record = manualRecordService.loadManualRecord(100L);

        Calendar calendar = new GregorianCalendar(2016, 1, 3);
        Date date = calendar.getTime();

        Assertions.assertEquals("manual 1", record.getDescription());
        Assertions.assertEquals(date, record.getBillingDate());
        Assertions.assertEquals(100, record.getId());
        Assertions.assertEquals(date, record.getCreationDate());
        Assertions.assertEquals(1, record.getBudgetId());
        Assertions.assertEquals(MoneyUtil.createMoneyFromCents(800), record.getMoneyAmount());
    }

    @Test
    @DatabaseSetup("manualRecords.xml")
    @DatabaseTearDown(value = "manualRecords.xml", type = DatabaseOperation.DELETE_ALL)
    void testLoadManualRecordNotExisting() {
        ManualRecord record = manualRecordService.loadManualRecord(6L);
        Assertions.assertNull(record);
    }

    @Test
    @DatabaseSetup("manualRecords.xml")
    @DatabaseTearDown(value = "manualRecords.xml", type = DatabaseOperation.DELETE_ALL)
    void testSaveManualRecord() {
        ManualRecord record = new ManualRecord();

        Calendar calendar = new GregorianCalendar(2016, 1, 3);
        Date date = calendar.getTime();

        record.setBillingDate(date);
        record.setBudgetId(1L);
        record.setDescription("save");
        record.setMoneyAmount(MoneyUtil.createMoneyFromCents(1000));

        long newId = manualRecordService.saveManualRecord(record);

        ManualRecord loaded = manualRecordService.loadManualRecord(newId);
        Assertions.assertEquals(loaded.getBudgetId(), record.getBudgetId());
        Assertions.assertEquals(loaded.getDescription(), record.getDescription());
        Assertions.assertEquals(loaded.getId(), newId);
        Assertions.assertEquals(loaded.getBillingDate(), date);
        Assertions.assertEquals(loaded.getMoneyAmount(), record.getMoneyAmount());
    }

    @Test
    @DatabaseSetup("manualRecords.xml")
    @DatabaseTearDown(value = "manualRecords.xml", type = DatabaseOperation.DELETE_ALL)
    void testDeleteRecord() {
        assertNotNull(manualRecordService.loadManualRecord(100L));
        manualRecordService.deleteRecord(100L);
        assertNull(manualRecordService.loadManualRecord(100L));
    }
}
