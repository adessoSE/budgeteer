package org.wickedsource.budgeteer.service.imports;


import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkRecordImportIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private ImportService importService;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private ApplicationContext applicationContext;

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
        List<ImportFile> importFiles = new ArrayList<ImportFile>();
        importFiles.add(new ImportFile("file1", getClass().getResourceAsStream("testReport1.xlsx")));
        importFiles.add(new ImportFile("file2", getClass().getResourceAsStream("testReport2.xlsx")));
        importService.doImport(1l, new AprodaWorkRecordsImporter(), importFiles);
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
        Iterator<WorkRecordEntity> iterator = workRecordRepository.findAll().iterator();
        while (iterator.hasNext()) {
            WorkRecordEntity record = iterator.next();
            Assert.assertNotNull(record.getBudget());
            Assert.assertNotNull(record.getPerson());
            Assert.assertNotNull(record.getDate());
            Assert.assertNotNull(record.getMinutes());
            if (hasDailyRate) {
                Assert.assertNotEquals(MoneyUtil.createMoneyFromCents(0), workRecordRepository.findAll().iterator().next().getDailyRate());
            } else {
                Assert.assertEquals(MoneyUtil.createMoneyFromCents(0), workRecordRepository.findAll().iterator().next().getDailyRate());
            }
        }
    }

    private void assertCounts() {
        Assert.assertEquals(16, workRecordRepository.count());
        Assert.assertEquals(2, budgetRepository.count());
        Assert.assertEquals(2, personRepository.count());
        Assert.assertEquals(1, importRepository.count());
    }

    @Test
    @DatabaseSetup("doImportWithData.xml")
    @DatabaseTearDown(value = "doImportWithData.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAndRemoveManuallyEditedEntries() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        WorkRecordDatabaseImporter importer = applicationContext.getBean(WorkRecordDatabaseImporter.class,1l, "Test");
        importer.setEarliestRecordDate(formatter.parse("2012-01-01"));
        importer.setLatestRecordDate(formatter.parse("2016-08-15"));

        List<List<String>> feedback = importer.findAndRemoveManuallyEditedEntries();
        ProjectEntity projectEntity = new ProjectEntity(); projectEntity.setId(1l);
        List<WorkRecordEntity> workRecordsInImportDateRange = workRecordRepository.findByProjectAndDateRange(projectEntity, formatter.parse("2012-01-01"), formatter.parse("2016-08-15"));
        Assert.assertEquals(7, workRecordsInImportDateRange.size());
        Assert.assertEquals(10, Lists.newArrayList(workRecordRepository.findAll()).size());
        Assert.assertEquals(5, feedback.size());
    }

}
