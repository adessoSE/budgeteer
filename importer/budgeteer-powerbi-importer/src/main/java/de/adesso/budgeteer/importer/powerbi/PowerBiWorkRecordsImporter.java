package de.adesso.budgeteer.importer.powerbi;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.*;

public class PowerBiWorkRecordsImporter implements WorkRecordsImporter {

  private static final String PROJECT_ID_COLUMN_NAME = "ProjektID";
  private static final String PROJECT_NAME_COLUMN_NAME = "Projekt";
  private static final String EMPLOYEE_NAME_COLUMN_NAME = "Mitarbeiter";
  private static final String DATE_COLUMN_NAME = "Datum";
  private static final String HOURS_COLUMN_NAME = "Erfasst (h)";
  private static final String INVOICEABLE_COLUMN_NAME = "Aktivität";

  private static final List<String> REQUIRED_COLUMNS =
      List.of(
          PROJECT_ID_COLUMN_NAME,
          PROJECT_NAME_COLUMN_NAME,
          EMPLOYEE_NAME_COLUMN_NAME,
          DATE_COLUMN_NAME,
          HOURS_COLUMN_NAME,
          INVOICEABLE_COLUMN_NAME);

  private List<List<String>> skippedRecords;

  public PowerBiWorkRecordsImporter() {
    this.skippedRecords = new ArrayList<>();
  }

  @Override
  public String getDisplayName() {
    return "Power BI Working Hours Importer";
  }

  @Override
  public List<String> getSupportedFileExtensions() {
    return List.of(".xlsx");
  }

  @Override
  public ExampleFile getExampleFile() {
    return null;
  }

  @Override
  public List<List<String>> getSkippedRecords() {
    return skippedRecords;
  }

  @Override
  public List<ImportedWorkRecord> importFile(ImportFile file)
      throws ImportException, InvalidFileFormatException {
    try (var workbook = new XSSFWorkbook(file.getInputStream())) {
      var powerBiHeaderRow = findPowerBiHeader(workbook);
      if (powerBiHeaderRow.isEmpty()) {
        throw new ImportException(
            String.format("%s is not a valid importable PowerBI file", file.getFilename()));
      }
      return importPowerBiWorkbook(workbook, powerBiHeaderRow.get());
    } catch (IOException e) {
      throw new ImportException(e);
    } catch (RuntimeException e) {
      throw new InvalidFileFormatException("Invalid file", file.getFilename(), e);
    }
  }

  private List<ImportedWorkRecord> importPowerBiWorkbook(Workbook workbook, int headerRowNum) {
    var sheet = workbook.getSheetAt(0);
    var columnHeaders = getHeaderColumns(sheet.getRow(headerRowNum));
    var invoiceableColumn = columnHeaders.get(INVOICEABLE_COLUMN_NAME);

    var splitByInvoiceableRows =
        StreamSupport.stream(sheet.spliterator(), false)
            .dropWhile(row -> row.getRowNum() <= headerRowNum)
            .collect(
                Collectors.groupingBy(
                    row ->
                        row.getCell(invoiceableColumn) != null
                            && isInvoiceable(row.getCell(invoiceableColumn).getStringCellValue())));

    this.skippedRecords =
        splitByInvoiceableRows.get(false).stream()
            .map(this::mapSkippedRecord)
            .collect(Collectors.toList());

    var projectIdColumn = columnHeaders.get(PROJECT_ID_COLUMN_NAME);
    var projectNameColumn = columnHeaders.get(PROJECT_NAME_COLUMN_NAME);
    var employeeNameColumn = columnHeaders.get(EMPLOYEE_NAME_COLUMN_NAME);
    var dateColumn = columnHeaders.get(DATE_COLUMN_NAME);
    var hoursColumn = columnHeaders.get(HOURS_COLUMN_NAME);

    return splitByInvoiceableRows.get(true).stream()
        .map(
            row ->
                mapWorkRecord(
                    row,
                    projectIdColumn,
                    projectNameColumn,
                    employeeNameColumn,
                    dateColumn,
                    hoursColumn))
        .collect(Collectors.toList());
  }

  Optional<Integer> findPowerBiHeader(Workbook workbook) {
    var sheet = workbook.getSheetAt(0);
    return StreamSupport.stream(sheet.spliterator(), false)
        .filter(this::isPowerBiHeader)
        .map(Row::getRowNum)
        .findFirst();
  }

  private Map<String, Integer> getHeaderColumns(Row row) {
    var headings = new HashMap<String, Integer>();
    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
      headings.put(row.getCell(i).getStringCellValue(), i);
    }
    return headings;
  }

  private boolean isPowerBiHeader(Row row) {
    var headerColumns = getHeaderColumns(row);
    return REQUIRED_COLUMNS.stream().allMatch(headerColumns::containsKey);
  }

  private ImportedWorkRecord mapWorkRecord(
      Row row,
      int projectIdColumn,
      int projectNameColumn,
      int employeeNameColumn,
      int dateColumn,
      int hoursColumn) {
    var prefixedProjectId = row.getCell(projectIdColumn).getStringCellValue();
    var projectId = prefixedProjectId.substring(prefixedProjectId.indexOf('A'));
    var projectName = row.getCell(projectNameColumn).getStringCellValue();
    var employee = row.getCell(employeeNameColumn).getStringCellValue();
    var date = row.getCell(dateColumn).getDateCellValue();
    var hours = row.getCell(hoursColumn).getNumericCellValue();
    return new ImportedWorkRecord(
        projectId, projectName, employee, date, (int) Math.round(hours * 60));
  }

  private List<String> mapSkippedRecord(Row row) {
    var cellValues =
        StreamSupport.stream(row.spliterator(), false)
            .map(cell -> cell == null ? "" : cell.toString());
    return Stream.concat(
            cellValues,
            Stream.of("", String.format("Line: %s", row.getRowNum()), "Record not importable"))
        .collect(Collectors.toList());
  }

  private boolean isInvoiceable(String value) {
    return "KV".equals(value);
  }
}
