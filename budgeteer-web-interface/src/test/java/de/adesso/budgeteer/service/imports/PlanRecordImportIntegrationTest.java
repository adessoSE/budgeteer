package de.adesso.budgeteer.service.imports;


import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adesso.budgeteer.IntegrationTestTemplate;
import de.adesso.budgeteer.importer.resourceplan.ResourcePlanImporter;
import de.adesso.budgeteer.imports.api.ImportException;
import de.adesso.budgeteer.imports.api.ImportFile;
import de.adesso.budgeteer.imports.api.InvalidFileFormatException;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.imports.ImportEntity;
import de.adesso.budgeteer.persistence.imports.ImportRepository;
import de.adesso.budgeteer.persistence.person.PersonRepository;
import de.adesso.budgeteer.persistence.record.PlanRecordEntity;
import de.adesso.budgeteer.persistence.record.PlanRecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class PlanRecordImportIntegrationTest extends IntegrationTestTemplate {

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

    @Test
    @DatabaseSetup("doImportWithEmptyDatabase.xml")
    @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
    void testDoImportWithEmptyDatabase() throws Exception {
        doImport();
        assertCounts(1);
        assertImportedRecords();
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingPersonsAndBudgets.xml")
    @DatabaseTearDown(value = "doImportWithExistingPersonsAndBudgets.xml", type = DatabaseOperation.DELETE_ALL)
    void testDoImportWithExistingPersonsAndBudgets() throws Exception {
        doImport();
        assertCounts(2);
        assertImportedRecords();
        assertImportRecord();
    }

    @Test
    @DatabaseSetup("doImportWithExistingDailyRates.xml")
    @DatabaseTearDown(value = "doImportWithExistingDailyRates.xml", type = DatabaseOperation.DELETE_ALL)
    void testDoImportWithExistingDailyRates() throws Exception {
        doImport();
        assertCounts(2);
        assertImportedRecords();
        assertImportRecord();
    }

    private void doImport() throws ImportException, InvalidFileFormatException {
        List<ImportFile> importFiles = new ArrayList<ImportFile>();
        importFiles.add(new ImportFile("resource_plan.xlsx", getClass().getResourceAsStream("resource_plan.xlsx")));
        importService.doImport(1L, new ResourcePlanImporter(), importFiles);
    }

    private void assertImportRecord() throws ParseException {
        ImportEntity importRecord = importRepository.findAll().iterator().next();
        Assertions.assertNotNull(importRecord.getProject());
        Assertions.assertNotNull(importRecord.getImportDate());
        Assertions.assertNotNull(importRecord.getImportType());
        Assertions.assertEquals(format.parse("01.01.2014"), importRecord.getStartDate());
        Assertions.assertEquals(format.parse("01.02.2014"), importRecord.getEndDate());
    }

    private void assertImportedRecords() {
        for (PlanRecordEntity record : planRecordRepository.findAll()) {
            Assertions.assertNotNull(record.getBudget());
            Assertions.assertNotNull(record.getPerson());
            Assertions.assertNotNull(record.getDate());
        }
    }

    private void assertCounts(int personCount) {
        Assertions.assertEquals(26, planRecordRepository.count());
        Assertions.assertEquals(2, budgetRepository.count());
        Assertions.assertEquals(personCount, personRepository.count());
        Assertions.assertEquals(1, importRepository.count());
        //Assertions.assertEquals(2, dailyRateRepository.count());
    }

}
