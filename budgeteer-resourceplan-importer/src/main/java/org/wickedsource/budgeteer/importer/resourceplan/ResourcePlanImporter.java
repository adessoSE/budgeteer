package org.wickedsource.budgeteer.importer.resourceplan;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.wickedsource.budgeteer.imports.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourcePlanImporter implements PlanRecordsImporter {

    private static int RESOURCE_PLAN_SHEET_INDEX = 0;

    private static int COLUMN_PERSON = 0;

    private static int COLUMN_BUDGET = 1;

    private static int COLUMN_DAILY_RATE = 2;

    private static int FIRST_ENTRY_COLUMN = 5;

    private static int FIRST_ENTRY_ROW = 1;

    private List<List<String>> skippedRecords = new LinkedList<List<String>>();
    private SimpleDateFormat format = new SimpleDateFormat();

    @Override
    public List<ImportedPlanRecord> importFile(ImportFile file, CurrencyUnit currencyUnit) throws ImportException, InvalidFileFormatException {
        skippedRecords.add(new LinkedList<String>());
        LinkedList<String> filenameList = new LinkedList<String>();
        filenameList.add(file.getFilename());
        skippedRecords.add(filenameList);

        try {
            List<ImportedPlanRecord> resultList = new ArrayList<ImportedPlanRecord>();
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            if (!isValid(workbook)) {
                throw new InvalidFileFormatException("Invalid file", file.getFilename());
            }
            Sheet sheet = workbook.getSheetAt(RESOURCE_PLAN_SHEET_INDEX);
            List<DateColumn> dateColumns = getDateColumns(sheet);
            int i = FIRST_ENTRY_ROW;
            Row row = sheet.getRow(i);
            while (row != null && row.getCell(0).getStringCellValue() != null) {
                List<ImportedPlanRecord> records = parseRow(row, dateColumns, currencyUnit, skippedRecords);
                resultList.addAll(records);
                row = sheet.getRow(++i);
            }
            return resultList;
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }

    private boolean isValid(Workbook workbook) {
        boolean result = workbook.getNumberOfSheets() >= RESOURCE_PLAN_SHEET_INDEX;
        if (result) {
            try {
                Sheet s = workbook.getSheetAt(RESOURCE_PLAN_SHEET_INDEX);
                Row r = s.getRow(FIRST_ENTRY_ROW - 1);
                if (r == null) {
                    result = false;
                } else {
                    result = r.getCell(COLUMN_PERSON).getStringCellValue().equals("Person") &&
                            r.getCell(COLUMN_BUDGET).getStringCellValue().equals("Budget");
                }
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    private List<DateColumn> getDateColumns(Sheet sheet) {
        List<DateColumn> columns = new ArrayList<DateColumn>();
        int i = FIRST_ENTRY_COLUMN;
        Cell dateCell = sheet.getRow(0).getCell(i);
        while (dateCell != null && dateCell.getDateCellValue() != null) {
            Date date = dateCell.getDateCellValue();
            DateColumn dateColumn = new DateColumn(date, i);
            columns.add(dateColumn);
            dateCell = sheet.getRow(0).getCell(++i);
        }
        return columns;
    }

    private List<ImportedPlanRecord> parseRow(Row row, List<DateColumn> dateColumns, CurrencyUnit currencyUnit, List<List<String>> skippedRecords) throws ImportException {
        List<ImportedPlanRecord> recordsList = new ArrayList<ImportedPlanRecord>();

        for (DateColumn dateColumn : dateColumns) {

            Cell hoursCell = row.getCell(dateColumn.getColumnIndex());
            if (hoursCell != null) {
                double hoursPlanned = 0d;
                try {
                    hoursPlanned = row.getCell(dateColumn.getColumnIndex()).getNumericCellValue();
                } catch (IllegalStateException e) {
                    CellReference ref = new CellReference(row.getCell(dateColumn.getColumnIndex()));
                    throw new ImportException(String.format("Error importing field in row %d and column %s", row.getRowNum() + 1, ref.getCellRefParts()[2]));
                }
                if (hoursPlanned > 0) {
                    int minutesPlanned = (int) (hoursPlanned * 60);
                    ImportedPlanRecord record = new ImportedPlanRecord();
                    record.setPersonName(row.getCell(COLUMN_PERSON).getStringCellValue());
                    record.setBudgetName(row.getCell(COLUMN_BUDGET).getStringCellValue());
                    record.setDailyRate(Money.of(currencyUnit, row.getCell(COLUMN_DAILY_RATE).getNumericCellValue()));
                    record.setMinutesPlanned(minutesPlanned);
                    record.setDate(dateColumn.getDate());
                    recordsList.add(record);
                } else {
                    List<String> skippedRow = new LinkedList<String>();
                    skippedRow.add(row.getCell(COLUMN_PERSON).getStringCellValue());
                    skippedRow.add(row.getCell(COLUMN_BUDGET).getStringCellValue());
                    skippedRow.add(row.getCell(COLUMN_DAILY_RATE).toString());
                    skippedRow.add("" + 0);
                    skippedRow.add(format.format(dateColumn.getDate()));
                    skippedRow.add("");
                    skippedRow.add("Column: " + dateColumn.getColumnIndex());
                    skippedRow.add("Record without hours");
                    skippedRecords.add(skippedRow);
                }
            }
        }

        return recordsList;
    }

    @Override
    public String getDisplayName() {
        return "Resource Plan Importer";
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return Arrays.asList(".xslx");
    }

    @Override
    public ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("resource_plan.xlsx");
        file.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        
        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/example_resource_plan.xlsx"));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            XSSFCell cell;
            int i = row.getLastCellNum();

            XSSFCellStyle style = workbook.createCellStyle();
            style.setDataFormat((short) 14);

            calendar.setTime(date);

            while (i <= 2) {
                cell = row.createCell(i);
                cell.setCellValue(calendar.getTime());
                cell.setCellStyle(style);

                XSSFRow tmpRow1 = sheet.getRow(1);
                XSSFRow tmpRow2 = sheet.getRow(2);
                XSSFCell tmpCell1 = tmpRow1.createCell(i);
                XSSFCell tmpCell2 = tmpRow2.createCell(i);

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    tmpCell1.setCellValue(0);
                    tmpCell2.setCellValue(0);
                } else {
                    tmpCell1.setCellValue(8);
                    tmpCell2.setCellValue(4);
                }

                calendar.add(Calendar.DATE, -1);
                i--;
            }

            File tmp = File.createTempFile("example_resource_plan", ".xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(tmp);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            file.setInputStream(new FileInputStream(tmp));
        } catch (IOException e) {
            e.printStackTrace();
            file.setInputStream(getClass().getResourceAsStream("/eexample_resource_plan.xlsx"));
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
}
