package de.adesso.budgeteer.importer.powerbi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;

class PowerBiWorkRecordsImporterTest {

  @Test
  void acceptsValidPowerBiImportHeader() throws IOException {
    var importer = new PowerBiWorkRecordsImporter();
    var workbook =
        new XSSFWorkbook(new FileInputStream("src/test/resources/power-bi-valid-header.xlsx"));

    var result = importer.isValidPowerBiFile(workbook);

    assertThat(result).isTrue();
  }

  @Test
  void rejectsInvalidPowerBiImportHeader() throws IOException {
    var importer = new PowerBiWorkRecordsImporter();
    var workbook =
        new XSSFWorkbook(new FileInputStream("src/test/resources/power-bi-invalid-header.xlsx"));

    var result = importer.isValidPowerBiFile(workbook);

    assertThat(result).isFalse();
  }

  @Test
  void importsValidPowerBiFile() throws IOException, ImportException, InvalidFileFormatException {
    var importer = new PowerBiWorkRecordsImporter();
    var inputStream = new FileInputStream("src/test/resources/power-bi-example.xlsx");

    var result = importer.importFile(new ImportFile("power-bi-example.xlsx", inputStream));

    var expectedImportRecord = new ImportedWorkRecord();
    expectedImportRecord.setBudgetName("Projekt2");
    expectedImportRecord.setPersonName("Mustermann, Max");
    expectedImportRecord.setDate(DateUtil.parseYYYYMMDDDate("2023/03/23"));
    expectedImportRecord.setMinutesWorked(480);

    assertThat(result.get(0)).isEqualToComparingFieldByField(expectedImportRecord);
    assertThat(importer.getSkippedRecords()).hasSize(1);
  }
}
