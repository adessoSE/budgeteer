package org.wickedsource.budgeteer.importer.aproda;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AprodaWorkRecordsImporter implements WorkRecordsImporter {

    private static int SHEET_INDEX = 2;

    private static int COLUMN_INVOICABLE = 8;

    private static int COLUMN_DATE = 1;

    private static int COLUMN_PERSON = 0;

    private static int COLUMN_BUDGET = 5;

    private static int COLUMN_HOURS = 7;

    @Override
    public List<ImportedWorkRecord> importFile(ImportFile file) throws ImportException {
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
        file.setInputStream(getClass().getResourceAsStream("/example_aproda_report.xlsx"));
        file.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return file;
    }

    public List<ImportedWorkRecord> read(ImportFile file) throws ImportException {
        try {
            List<ImportedWorkRecord> resultList = new ArrayList<ImportedWorkRecord>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            int i = 3;
            Row row = sheet.getRow(i);
            while (row != null && row.getCell(0).getStringCellValue() != null) {
                if (isInvoicable(row)) {
                    // only import hours that are invoicable
                    ImportedWorkRecord record = parseRow(row, file);
                    resultList.add(record);
                }
                i++;
                row = sheet.getRow(i);
            }
            return resultList;
        } catch (IOException e) {
            throw new ImportException(e);
        }
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

            if (isInvoicable(row)) {
                if (record.getDate() == null) {
                    throw new ImportException(String.format("Missing date in row %d and column %d of file %s", row.getRowNum() + 1, COLUMN_DATE + 1, file.getFilename()));
                }
                if (record.getBudgetName() == null || "".equals(record.getBudgetName())) {
                    throw new ImportException(String.format("Missing budget name or id in row %d and column %d of file %s", row.getRowNum() + 1, COLUMN_BUDGET + 1, file.getFilename()));
                }
                if (record.getPersonName() == null || "".equals(record.getPersonName())) {
                    throw new ImportException((String.format("Missing person name in row %d and column %d of file %s", row.getRowNum() + 1, COLUMN_PERSON + 1, file.getFilename())));
                }
            }

            return record;
        } catch (ImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ImportException(e);
        }
    }

    private boolean isInvoicable(Row row) {
        return "ja".equalsIgnoreCase(row.getCell(COLUMN_INVOICABLE).getStringCellValue());
    }
}
