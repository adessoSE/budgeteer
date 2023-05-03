package de.adesso.budgeteer.importer.powerbi;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.wickedsource.budgeteer.imports.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PowerBiWorkRecordsImporter implements WorkRecordsImporter {

    private static final List<String> HEADER = List.of(
            "ProjektID",
            "Projekt",
            "Mitarbeiter",
            "Datum",
            "Ort",
            "Arbeitsauftrag",
            "Tätigkeitsbeschreibung",
            "Erfasst (h)",
            "Aktivität",
            "MitarbeiterStatus",
            "WorkflowStatus"
    );
    private static final int PROJECT_ID_COLUMN = 1;
    private static final int EMPLOYEE_COLUMN = 2;
    private static final int DATE_COLUMN = 3;
    private static final int HOURS_COLUMN = 7;
    private static final int INVOICEABLE_COLUMN = 8;
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
    public List<ImportedWorkRecord> importFile(ImportFile file) throws ImportException, InvalidFileFormatException {
        try (var workbook = new XSSFWorkbook(file.getInputStream())) {
            if (!isValidPowerBiFile(workbook)) {
                throw new ImportException(String.format("%s is not a valid importable PowerBI file", file.getFilename()));
            }
            return importPowerBiWorkbook(workbook);
        } catch (IOException e) {
            throw new ImportException(e);
        } catch (RuntimeException e) {
            throw new InvalidFileFormatException("Invalid file", file.getFilename(), e);
        }
    }

    private List<ImportedWorkRecord> importPowerBiWorkbook(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        var splitByInvoiceableRows = StreamSupport.stream(sheet.spliterator(), false)
                .dropWhile(Predicate.not(this::isPowerBiHeader))
                .skip(1)
                .collect(Collectors.groupingBy(row -> isInvoiceable(row.getCell(INVOICEABLE_COLUMN).getStringCellValue())));
        this.skippedRecords = splitByInvoiceableRows.get(false).stream().map(this::mapSkippedRecord).collect(Collectors.toList());

        return splitByInvoiceableRows.get(true).stream()
                .map(this::mapWorkRecord)
                .collect(Collectors.toList());
    }

    boolean isValidPowerBiFile(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        return StreamSupport.stream(sheet.spliterator(), false).anyMatch(this::isPowerBiHeader);
    }

    private boolean isPowerBiHeader(Row row) {
        for (int i = 0; i < HEADER.size(); i++) {
            var cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                return false;
            }
            if (!HEADER.get(i).equals(cell.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    private ImportedWorkRecord mapWorkRecord(Row row) {
        var projectId = row.getCell(PROJECT_ID_COLUMN).getStringCellValue();
        var employee = row.getCell(EMPLOYEE_COLUMN).getStringCellValue();
        var date = row.getCell(DATE_COLUMN).getDateCellValue();
        var hours = row.getCell(HOURS_COLUMN).getNumericCellValue();
        return new ImportedWorkRecord(projectId, employee, date, (int) Math.round(hours * 60));
    }

    private List<String> mapSkippedRecord(Row row) {
        var cellValues = StreamSupport.stream(row.spliterator(), false).map(cell -> cell == null ? "" : cell.toString());
        return Stream.concat(cellValues, Stream.of("", String.format("Line: %s", row.getRowNum()), "Record not importable"))
                .collect(Collectors.toList());
    }

    private boolean isInvoiceable(String value) {
        return "KV".equals(value);
    }
}
