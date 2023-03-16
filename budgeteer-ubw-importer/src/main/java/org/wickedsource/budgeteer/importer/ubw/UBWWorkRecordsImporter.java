package org.wickedsource.budgeteer.importer.ubw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.wickedsource.budgeteer.imports.api.*;

public class UBWWorkRecordsImporter implements WorkRecordsImporter {

  private int sheetIndex = -1;

  private int columnInvoiceable = 11;

  private static final int COLUMN_DATE = 3;

  private static final int COLUMN_PERSON = 2;

  private int columnBudget = 8;

  private int columnHours = 10;

  private List<List<String>> skippedRecords = new LinkedList<>();

  @Override
  public List<ImportedWorkRecord> importFile(ImportFile file)
      throws ImportException, InvalidFileFormatException {
    skippedRecords.add(new LinkedList<>());
    // Adds the name of the imported file at the beginning of the list of skipped data sets..
    skippedRecords.add(List.of(file.getFilename()));

    List<ImportedWorkRecord> records = new ArrayList<>();
    Workbook workbook;
    try {
      workbook = new XSSFWorkbook(file.getInputStream());
    } catch (IOException e) {
      throw new ImportException(e);
    }
    if (!checkValidityAndSetSheetAndColumnIndices(workbook)) {
      throw new InvalidFileFormatException("Invalid file", file.getFilename());
    }
    Sheet sheet = workbook.getSheetAt(sheetIndex);
    for (int i = 3; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (!isImportable(row)) {
        if (!isCompletelyEmpty(row)) {
          skippedRecords.add(getRowAsStrings(row, i));
        }
        continue;
      }
      records.add(parseRow(row, file));
    }
    return records;
  }

  private boolean isCompletelyEmpty(Row row) {
    if (row == null) {
      return true;
    }
    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
      Cell cell = row.getCell(i);
      if (cell != null && !isBlank(cell.toString())) {
        return false;
      }
    }
    return true;
  }

  private boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  @Override
  public String getDisplayName() {
    return "UBW Working Hours Importer";
  }

  @Override
  public List<String> getSupportedFileExtensions() {
    return Collections.singletonList(".xlsx");
  }

  @Override
  public ExampleFile getExampleFile() {
    ExampleFile file = new ExampleFile();
    file.setFileName("ubw_report.xlsx");
    file.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    try {
      Date date = new Date();
      Calendar maxCalendar = Calendar.getInstance();
      Calendar maxineCalendar = Calendar.getInstance();
      XSSFWorkbook workbook =
          new XSSFWorkbook(getClass().getResourceAsStream("/example_ubw_report.xlsx"));
      checkValidityAndSetSheetAndColumnIndices(workbook);
      XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
      XSSFRow row;
      XSSFCell cell;
      int col = 3;
      int i = sheet.getLastRowNum();

      XSSFCellStyle style = workbook.createCellStyle();
      style.setBorderBottom(BorderStyle.THIN);
      style.setBorderTop(BorderStyle.THIN);
      style.setBorderRight(BorderStyle.THIN);
      style.setBorderLeft(BorderStyle.THIN);
      style.setDataFormat((short) 14);

      maxCalendar.setTime(date);
      maxineCalendar.setTime(date);

      while (i != 0) {
        row = sheet.getRow(i);

        if (row.getCell(col - 1).getStringCellValue().equals("Mustermann, Max")) {
          if (maxCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            maxCalendar.add(Calendar.DATE, -1);
          } else if (maxCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            maxCalendar.add(Calendar.DATE, -2);
          }
          cell = row.createCell(3);
          cell.setCellValue(maxCalendar.getTime());
          cell.setCellStyle(style);
          maxCalendar.add(Calendar.DATE, -1);
        } else if (row.getCell(col - 1).getStringCellValue().equals("Mustermann, Maxine")) {
          if (maxineCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            maxineCalendar.add(Calendar.DATE, -1);
          } else if (maxineCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            maxineCalendar.add(Calendar.DATE, -2);
          }
          cell = row.createCell(3);
          cell.setCellValue(maxineCalendar.getTime());
          cell.setCellStyle(style);
          maxineCalendar.add(Calendar.DATE, -1);
        }
        i--;
      }

      File tmp = File.createTempFile("example_ubw_report", ".xlsx");
      FileOutputStream fileOutputStream = new FileOutputStream(tmp);
      workbook.write(fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();

      file.setInputStream(new FileInputStream(tmp));
    } catch (IOException e) {
      e.printStackTrace();
      file.setInputStream(getClass().getResourceAsStream("/example_ubw_report.xlsx"));
    }
    return file;
  }

  @Override
  public List<List<String>> getSkippedRecords() {
    // if just an empty row at the beginning and the filename is in the List of skipped records,
    // return an empty List
    if (skippedRecords != null && skippedRecords.size() == 2) {
      skippedRecords = new LinkedList<>();
    }
    return skippedRecords;
  }

  boolean checkValidityAndSetSheetAndColumnIndices(Workbook workbook) {
    boolean isValid = false;
    for (int i = 0; i < workbook.getNumberOfSheets() && !isValid; i++) {
      Sheet sheet = workbook.getSheetAt(i);
      int headerRowIndex = 2;
      if (sheet.getRow(headerRowIndex) == null) {
        isValid = false;
      } else {
        Row r = sheet.getRow(headerRowIndex);
        try {
          if (r.getCell(5).getStringCellValue().equals("AA-Nr")) {
            columnBudget = 9;
            columnHours = 11;
            columnInvoiceable = 12;
          } else {
            columnBudget = 8;
            columnHours = 10;
            columnInvoiceable = 11;
          }
          isValid =
              r.getCell(COLUMN_PERSON).getStringCellValue().equals("Name")
                  && r.getCell(COLUMN_DATE).getStringCellValue().equals("Tag")
                  && r.getCell(columnBudget).getStringCellValue().equals("Subgruppe")
                  && r.getCell(columnHours).getStringCellValue().equals("Aufwand [h]")
                  && r.getCell(columnInvoiceable).getStringCellValue().equals("KV");
          sheetIndex = i;
        } catch (Exception e) {
          isValid = false;
        }
      }
    }
    return isValid;
  }

  private List<String> getRowAsStrings(Row row, int index) {
    List<String> result = new LinkedList<>();
    for (short i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
      Cell cell = row.getCell(i);
      if (cell == null) {
        result.add("");
        continue;
      }
      result.add(cell.toString());
    }
    result.add("");
    result.add("Line: " + (index + 1));
    result.add("Record is not importable");
    return result;
  }

  private ImportedWorkRecord parseRow(Row row, ImportFile file) throws ImportException {
    try {
      String personName = row.getCell(COLUMN_PERSON).getStringCellValue();
      Date date = row.getCell(COLUMN_DATE).getDateCellValue();
      String budgetName = row.getCell(columnBudget).getStringCellValue();
      double hours = row.getCell(columnHours).getNumericCellValue();

      var importedWorkRecord = new ImportedWorkRecord();
      importedWorkRecord.setDate(date);
      importedWorkRecord.setBudgetName(budgetName);
      importedWorkRecord.setPersonName(personName);
      importedWorkRecord.setMinutesWorked((int) Math.round(hours * 60));

      if (importedWorkRecord.getDate() == null) {
        throw new ImportException(
            String.format(
                "Missing date in row %d and column %d of file %s",
                row.getRowNum() + 1, COLUMN_DATE + 1, file.getFilename()));
      }

      return importedWorkRecord;
    } catch (ImportException e) {
      throw e;
    } catch (Exception e) {
      throw new ImportException(e);
    }
  }

  private boolean isImportable(Row row) {
    return row != null
        && ("ja"
            .equalsIgnoreCase(
                row.getCell(columnInvoiceable, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    .getStringCellValue()))
        && (row.getCell(columnBudget, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null)
        && (row.getCell(COLUMN_PERSON, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null);
  }
}
