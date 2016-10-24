package org.wickedsource.budgeteer.importer.ubw;

import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.*;

public class UBWWorkRecordsImporter implements WorkRecordsImporter {

    private static final int SHEET_INDEX = 2;

    private static final int COLUMN_PERSON = 2;

    private static final int COLUMN_DATE = 3;

    private static final int COLUMN_BUDGET = 8;

    private static final int COLUMN_HOURS = 10;

    private static final int COLUMN_INVOICABLE = 11;

    private List<List<String>> skippedRecords = new LinkedList<List<String>>();

    @Override
    public List<ImportedWorkRecord> importFile(ImportFile file) throws ImportException, InvalidFileFormatException {
        try {
            skippedRecords.add(new LinkedList<String>());
            //Adds the name of the imported file at the beginning of the list of skipped data sets..
            List<String> fileName = new LinkedList<String>();
            fileName.add(file.getFilename());
            skippedRecords.add(fileName);

            List<ImportedWorkRecord> resultList = new ArrayList<ImportedWorkRecord>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            if(!checkValidity(workbook)){
                throw new InvalidFileFormatException("Invalid file", file.getFilename());
            }
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            int rowIndex = 3; // First row with valid values
            Row row = sheet.getRow(rowIndex);
            while (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue() != null) {
                if (isImportable(row)) {
                    ImportedWorkRecord record = parseRow(row, file);
                    resultList.add(record);
                } else {
                    if(!isCompletelyEmpty(row)) {
                        skippedRecords.add(getRowAsStrings(row, rowIndex));
                    }
                }
                rowIndex++;
                row = sheet.getRow(rowIndex);
            }
            return resultList;
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }

    private boolean isCompletelyEmpty(Row row) {
        for(short i=row.getFirstCellNum(); i<row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (!isBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    private boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }

    @Override
    public String getDisplayName() {
        return "UBW Working Hours Importer";
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return Arrays.asList(".xlsx");
    }

    @Override
    public ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("ubw_report.xlsx");
        file.setInputStream(getClass().getResourceAsStream("/example_ubw_report.xlsx"));
        file.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return file;
    }

    @Override
    public List<List<String>> getSkippedRecords() {
        //if just an empty row at the beginning and the filename is in the List of skipped records, return an empty List
        if(skippedRecords != null && skippedRecords.size() == 2){
            skippedRecords = new LinkedList<List<String>>();
        }
        return skippedRecords;
    }


    boolean checkValidity(Workbook workbook) {
        boolean isValid = workbook.getNumberOfSheets() >= SHEET_INDEX ;
        if(isValid){
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            int headerRowIndex = 2;
            if(sheet.getRow(headerRowIndex) == null){
                isValid = false ;
            } else {
                Row r = sheet.getRow(headerRowIndex);
                try {
                    isValid = r.getCell(COLUMN_PERSON).getStringCellValue().equals("Name") &&
                            r.getCell(COLUMN_DATE).getStringCellValue().equals("Tag") &&
                            r.getCell(COLUMN_BUDGET).getStringCellValue().equals("Subgruppe") &&
                            r.getCell(COLUMN_HOURS).getStringCellValue().equals("Aufwand [h]")&&
                            r.getCell(COLUMN_INVOICABLE).getStringCellValue().equals("KV");
                }catch (Exception e){
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private List<String> getRowAsStrings(Row row, int index) {
        List<String> result = new LinkedList<String>();
        for(short i=row.getFirstCellNum(); i<row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if(cell == null) {
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
