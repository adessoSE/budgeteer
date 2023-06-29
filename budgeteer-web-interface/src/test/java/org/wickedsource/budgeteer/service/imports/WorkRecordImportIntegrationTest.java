package org.wickedsource.budgeteer.service.imports;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.imports.ImportEntity;
import de.adesso.budgeteer.persistence.imports.ImportRepository;
import de.adesso.budgeteer.persistence.person.PersonRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;

class WorkRecordImportIntegrationTest extends IntegrationTestTemplate {

  private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

  @Autowired private ImportService importService;

  @Autowired private WorkRecordRepository workRecordRepository;

  @Autowired private BudgetRepository budgetRepository;

  @Autowired private PersonRepository personRepository;

  @Autowired private ImportRepository importRepository;

  @Autowired private ApplicationContext applicationContext;

  @Test
  @DatabaseSetup("doImportWithEmptyDatabase.xml")
  @DatabaseTearDown(value = "doImportWithEmptyDatabase.xml", type = DatabaseOperation.DELETE_ALL)
  void testDoImportWithEmptyDatabase() throws Exception {
    doImport();
    assertCounts();
    assertImportedRecords(false);
    assertImportRecord();
  }

  @Test
  @DatabaseSetup("doImportWithExistingPersonsAndBudgets.xml")
  @DatabaseTearDown(
      value = "doImportWithExistingPersonsAndBudgets.xml",
      type = DatabaseOperation.DELETE_ALL)
  void testDoImportWithExistingPersonsAndBudgets() throws Exception {
    doImport();
    assertCounts();
    assertImportedRecords(false);
    assertImportRecord();
  }

  @Test
  @DatabaseSetup("doImportWithExistingDailyRates.xml")
  @DatabaseTearDown(
      value = "doImportWithExistingDailyRates.xml",
      type = DatabaseOperation.DELETE_ALL)
  void testDoImportWithExistingDailyRates() throws Exception {
    doImport();
    assertCounts();
    assertImportedRecords(true);
    assertImportRecord();
  }

  private void doImport() throws ImportException, InvalidFileFormatException {
    List<ImportFile> importFiles = new ArrayList<ImportFile>();
    importFiles.add(new ImportFile("file1", getClass().getResourceAsStream("testReport1.xlsx")));
    importFiles.add(new ImportFile("file2", getClass().getResourceAsStream("testReport2.xlsx")));
    importService.doImport(1L, new AprodaWorkRecordsImporter(), importFiles);
  }

  private void assertImportRecord() throws ParseException {
    ImportEntity importRecord = importRepository.findAll().iterator().next();
    Assertions.assertNotNull(importRecord.getProject());
    Assertions.assertNotNull(importRecord.getImportDate());
    Assertions.assertNotNull(importRecord.getImportType());
    Assertions.assertEquals(format.parse("06.10.2014"), importRecord.getStartDate());
    Assertions.assertEquals(format.parse("31.10.2014"), importRecord.getEndDate());
  }

  private void assertImportedRecords(boolean hasDailyRate) {
    for (WorkRecordEntity record : workRecordRepository.findAll()) {
      Assertions.assertNotNull(record.getBudget());
      Assertions.assertNotNull(record.getPerson());
      Assertions.assertNotNull(record.getDate());
      if (hasDailyRate) {
        Assertions.assertNotEquals(
            MoneyUtil.createMoneyFromCents(0),
            workRecordRepository.findAll().iterator().next().getDailyRate());
      } else {
        Assertions.assertEquals(
            MoneyUtil.createMoneyFromCents(0),
            workRecordRepository.findAll().iterator().next().getDailyRate());
      }
    }
  }

  private void assertCounts() {
    Assertions.assertEquals(16, workRecordRepository.count());
    Assertions.assertEquals(2, budgetRepository.count());
    Assertions.assertEquals(2, personRepository.count());
    Assertions.assertEquals(1, importRepository.count());
  }
}
