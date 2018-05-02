package org.wickedsource.budgeteer.importer.ubw;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

class UBWWorkRecordsImporterTest {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    void testRead() throws Exception {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", importer.getExampleFile().getInputStream()));
        assertEquals(15, records.size());
        assertEquals("Mustermann, Max", records.get(0).getPersonName());
        assertEquals("Testmanagement", records.get(0).getBudgetName());
        assertEquals(180d, records.get(0).getMinutesWorked(), 1d);
        assertEquals(format.parse("06.07.2016"), records.get(0).getDate());
    }

    @Test
    void testGetSkippedDataSets() throws Exception {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        importer.importFile(new ImportFile("file.xslx", importer.getExampleFile().getInputStream()));
        List<List<String>> skippedRecords = importer.getSkippedRecords();
        assertEquals(36, skippedRecords.size());

        assertEquals("file.xslx", skippedRecords.get(1).get(0));
    }

    @Test
    void testGetExampleFile(){
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        ExampleFile file = importer.getExampleFile();
        assertNotNull(file.getFileName());
        assertNotNull(file.getInputStream());
        assertNotNull(file.getContentType());
    }

    @Test
    void testValidity() throws IOException {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        Workbook workbook = new XSSFWorkbook(importer.getExampleFile().getInputStream());
        assertTrue(importer.checkValidity(workbook));
    }
}
