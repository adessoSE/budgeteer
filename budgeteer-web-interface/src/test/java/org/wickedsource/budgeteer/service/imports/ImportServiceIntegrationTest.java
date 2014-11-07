package org.wickedsource.budgeteer.service.imports;


import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportServiceIntegrationTest extends IntegrationTestTemplate{

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private ImportService importService;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportRepository importRepository;

    @Test
    @DatabaseSetup("doImportWithEmptyDatabase.xml")
    @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithEmptyDatabase() throws Exception {
        doImport();
        assertCounts();
        assertImportedRecords(false);
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingPersonsAndBudgets.xml")
    @DatabaseTearDown(value = "doImportWithExistingPersonsAndBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithExistingPersonsAndBudgets() throws Exception {
        doImport();
        assertCounts();
        assertImportedRecords(false);
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingDailyRates.xml")
    @DatabaseTearDown(value = "doImportWithExistingDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithExistingDailyRates() throws Exception {
        doImport();
        assertCounts();
        assertImportedRecords(true);
        assertImportRecord();
    }

    private void doImport() throws ImportException {
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        inputStreams.add(getClass().getResourceAsStream("testReport1.xlsx"));
        inputStreams.add(getClass().getResourceAsStream("testReport2.xlsx"));
        importService.doImport(1l, new AprodaWorkRecordsImporter(), inputStreams);
    }

    private void assertImportRecord() throws ParseException {
        ImportEntity importRecord = importRepository.findAll().iterator().next();
        Assert.assertNotNull(importRecord.getProject());
        Assert.assertNotNull(importRecord.getImportDate());
        Assert.assertNotNull(importRecord.getImportType());
        Assert.assertEquals(format.parse("06.10.2014"), importRecord.getStartDate());
        Assert.assertEquals(format.parse("31.10.2014"), importRecord.getEndDate());
    }

    private void assertImportedRecords(boolean hasDailyRate) {
        Iterator<WorkRecordEntity> iterator = recordRepository.findAll().iterator();
        while (iterator.hasNext()) {
            WorkRecordEntity record = iterator.next();
            Assert.assertNotNull(record.getBudget());
            Assert.assertNotNull(record.getPerson());
            Assert.assertNotNull(record.getDate());
            Assert.assertNotNull(record.getSpentMinutes());
            if (hasDailyRate) {
                Assert.assertNotEquals(MoneyUtil.createMoneyFromCents(0), recordRepository.findAll().iterator().next().getDailyRate());
            } else {
                Assert.assertEquals(MoneyUtil.createMoneyFromCents(0), recordRepository.findAll().iterator().next().getDailyRate());
            }
        }
    }

    private void assertCounts() {
        Assert.assertEquals(16, recordRepository.count());
        Assert.assertEquals(2, budgetRepository.count());
        Assert.assertEquals(2, personRepository.count());
        Assert.assertEquals(1, importRepository.count());
    }

}
