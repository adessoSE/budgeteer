package org.wickedsource.budgeteer.aproda;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AprodaWorkRecordsImporter implements WorkRecordsImporter {

    @Override
    public List<ImportedWorkRecord> importFile(byte[] file) throws ImportException {
        return importFile(new ByteArrayInputStream(file));
    }

    @Override
    public List<ImportedWorkRecord> importFile(InputStream in) throws ImportException {
        return read(in);
    }

    @Override
    public String getDisplayName() {
        return "Aproda Working Hours Importer";
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return Arrays.asList(".xlsx");
    }

    public List<ImportedWorkRecord> read(InputStream in) throws ImportException {
        try {
            List<ImportedWorkRecord> resultList = new ArrayList<ImportedWorkRecord>();
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(2);
            int i = 3;
            Row row = sheet.getRow(i);
            while (row != null && row.getCell(0).getStringCellValue() != null) {
                ImportedWorkRecord record = parseRow(row);
                resultList.add(record);
                i++;
                row = sheet.getRow(i);
            }
            return resultList;
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }

    private ImportedWorkRecord parseRow(Row row) throws ImportException {
        try {
            String personName = row.getCell(0).getStringCellValue();
            Date date = row.getCell(1).getDateCellValue();
            String budgetName = row.getCell(4).getStringCellValue();
            double hours = row.getCell(7).getNumericCellValue();

            ImportedWorkRecord record = new ImportedWorkRecord();
            record.setDate(date);
            record.setBudgetName(budgetName);
            record.setPersonName(personName);
            record.setMinutesWorked((int) Math.round(hours * 60));

            return record;
        } catch (Exception e) {
            throw new ImportException(e);
        }
    }
}
