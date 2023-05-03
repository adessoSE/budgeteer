package org.wickedsource.budgeteer.importer.aproda;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.wickedsource.budgeteer.imports.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class AprodaWorkRecordsImporter implements WorkRecordsImporter {

    private static int SHEET_INDEX = 2;

    private static int COLUMN_INVOICABLE = 8;

    private static int COLUMN_DATE = 1;

    private static int COLUMN_PERSON = 0;

    private static int COLUMN_BUDGET = 5;

    private static int COLUMN_HOURS = 7;

    private List<List<String>> skippedRecords = new LinkedList<List<String>>();

    @Override
    public List<ImportedWorkRecord> importFile(ImportFile file) throws ImportException, InvalidFileFormatException {
        return read(file);
    }

    @Override
    public String getDisplayName() {
        return "Aproda Working Hours Importer";
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return Arrays.asList(".xlsx");
    }

    @Override
    public ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("aproda_report.xlsx");
        file.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/example_aproda_report.xlsx"));
            XSSFSheet sheet = workbook.getSheetAt(2);
            XSSFRow row;
            XSSFCell cell;
            int i = sheet.getLastRowNum();

            XSSFCellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setDataFormat((short) 14);

            calendar.setTime(date);

            while (i != 0) {
                row = sheet.getRow(i);

                if (row.getCell(0).getStringCellValue().equals("Potter, Harry")) {
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        calendar.add(Calendar.DATE, -1);
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        calendar.add(Calendar.DATE, -2);
                    }

                    cell = row.createCell(1);
                    cell.setCellValue(calendar.getTime());
                    cell.setCellStyle(style);
                    calendar.add(Calendar.DATE, -1);
                }

                i--;
            }

            File tmp = File.createTempFile("example_aproda_report", ".xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(tmp);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            file.setInputStream(new FileInputStream(tmp));
        } catch (IOException e) {
            e.printStackTrace();
            file.setInputStream(getClass().getResourceAsStream("/example_aproda_report.xlsx"));
        }
        return file;
    }

    @Override
    public List<List<String>> getSkippedRecords() {
        //if just an empty row at the beginning and the filename is in the List of skipped records, return an empty List
        if (skippedRecords != null && skippedRecords.size() == 2) {
            skippedRecords = new LinkedList<List<String>>();
        }
        return skippedRecords;
    }

    public List<ImportedWorkRecord> read(ImportFile file) throws ImportException, InvalidFileFormatException {
        try {
            skippedRecords.add(new LinkedList<String>());
            //Adds the name of the imported file at the beginning of the list of skipped data sets..
            List<String> fileName = new LinkedList<String>();
            fileName.add(file.getFilename());
            skippedRecords.add(fileName);

            List<ImportedWorkRecord> resultList = new ArrayList<ImportedWorkRecord>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            if (!checkValidity(workbook)) {
                throw new InvalidFileFormatException("Invalid file", file.getFilename());
            }
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            int i = 3;
            Row row = sheet.getRow(i);
            while (row != null && row.getCell(0).getStringCellValue() != null) {
                if (isImportable(row)) {
                    ImportedWorkRecord record = parseRow(row, file);
                    resultList.add(record);
                } else {
                    skippedRecords.add(getRowAsStrings(row, i));
                }
                i++;
                row = sheet.getRow(i);
            }
            return resultList;
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }

    private boolean checkValidity(Workbook workbook) {
        boolean isValid = workbook.getNumberOfSheets() >= SHEET_INDEX;
        if (isValid) {
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            if (sheet.getRow(2) == null) {
                isValid = false;
            } else {
                Row r = sheet.getRow(2);
                try {
                    isValid = r.getCell(COLUMN_PERSON).getStringCellValue().equals("Name") &&
                            r.getCell(COLUMN_DATE).getStringCellValue().equals("Tag") &&
                            r.getCell(COLUMN_BUDGET).getStringCellValue().equals("Subgruppe") &&
                            r.getCell(COLUMN_HOURS).getStringCellValue().equals("Aufwand [h]");
                } catch (Exception e) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private List<String> getRowAsStrings(Row row, int index) {
        List<String> result = new LinkedList<String>();
        for (short i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                result.add("");
                continue;
            }
            result.add(cell.toString());
        }
        result.add("");
        result.add("Line: " + index);
        result.add("Record is not importable");
        return result;
    }

    private ImportedWorkRecord parseRow(Row row, ImportFile file) throws ImportException {
        try {
            String personName = row.getCell(COLUMN_PERSON).getStringCellValue();
            Date date = row.getCell(COLUMN_DATE).getDateCellValue();
            String budgetName = row.getCell(COLUMN_BUDGET).getStringCellValue();
            double hours = row.getCell(COLUMN_HOURS).getNumericCellValue();

            ImportedWorkRecord record = new ImportedWorkRecord();
            record.setDate(date);
            record.setBudgetName(budgetName);
            record.setPersonName(personName);
            record.setMinutesWorked((int) Math.round(hours * 60));

            if (record.getDate() == null) {
                throw new ImportException(String.format("Missing date in row %d and column %d of file %s", row.getRowNum() + 1, COLUMN_DATE + 1, file.getFilename()));
            }

            return record;
        } catch (ImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ImportException(e);
        }
    }

    private boolean isImportable(Row row) {
        return row != null && ("ja".equalsIgnoreCase(row.getCell(COLUMN_INVOICABLE).getStringCellValue()))
                && (row.getCell(COLUMN_BUDGET).getStringCellValue() != null)
                && (!"".equals(row.getCell(COLUMN_BUDGET).getStringCellValue().trim()))
                && (row.getCell(COLUMN_PERSON).getStringCellValue() != null)
                && (!"".equals(row.getCell(COLUMN_PERSON).getStringCellValue().trim()));
    }
}
