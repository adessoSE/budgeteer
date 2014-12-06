package org.wickedsource.budgeteer.service.imports;


import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.importer.resourceplan.ResourcePlanImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.record.PlanRecordEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlanRecordImportIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private ImportService importService;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private DailyRateRepository dailyRateRepository;

    @Test
    @DatabaseSetup("doImportWithEmptyDatabase.xml")
    @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithEmptyDatabase() throws Exception {
        doImport();
        assertCounts(1);
        assertImportedRecords();
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingPersonsAndBudgets.xml")
    @DatabaseTearDown(value = "doImportWithExistingPersonsAndBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithExistingPersonsAndBudgets() throws Exception {
        doImport();
        assertCounts(2);
        assertImportedRecords();
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingDailyRates.xml")
    @DatabaseTearDown(value = "doImportWithExistingDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDoImportWithExistingDailyRates() throws Exception {
        doImport();
        assertCounts(2);
        assertImportedRecords();
        assertImportRecord();
    }

    private void doImport() throws ImportException {
        List<ImportFile> importFiles = new ArrayList<ImportFile>();
        importFiles.add(new ImportFile("resource_plan.xlsx", getClass().getResourceAsStream("resource_plan.xlsx")));
        importService.doImport(1l, new ResourcePlanImporter(), importFiles);
    }

    private void assertImportRecord() throws ParseException {
        ImportEntity importRecord = importRepository.findAll().iterator().next();
        Assert.assertNotNull(importRecord.getProject());
        Assert.assertNotNull(importRecord.getImportDate());
        Assert.assertNotNull(importRecord.getImportType());
        Assert.assertEquals(format.parse("01.01.2014"), importRecord.getStartDate());
        Assert.assertEquals(format.parse("01.02.2014"), importRecord.getEndDate());
    }

    private void assertImportedRecords() {
        Iterator<PlanRecordEntity> iterator = planRecordRepository.findAll().iterator();
        while (iterator.hasNext()) {
            PlanRecordEntity record = iterator.next();
            Assert.assertNotNull(record.getBudget());
            Assert.assertNotNull(record.getPerson());
            Assert.assertNotNull(record.getDate());
            Assert.assertNotNull(record.getMinutes());
        }
    }

    private void assertCounts(int personCount) {
        Assert.assertEquals(26, planRecordRepository.count());
        Assert.assertEquals(2, budgetRepository.count());
        Assert.assertEquals(personCount, personRepository.count());
        Assert.assertEquals(1, importRepository.count());
        Assert.assertEquals(2, dailyRateRepository.count());
    }

}
