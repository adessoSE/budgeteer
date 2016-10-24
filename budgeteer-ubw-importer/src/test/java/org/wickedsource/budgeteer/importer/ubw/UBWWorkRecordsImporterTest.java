package org.wickedsource.budgeteer.importer.ubw;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;

public class UBWWorkRecordsImporterTest {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    public void testRead() throws Exception {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", importer.getExampleFile().getInputStream()));
        assertEquals(148, records.size());
        assertEquals("Nachname1, Vorname1", records.get(0).getPersonName());
        assertEquals("Budget 1", records.get(0).getBudgetName());
        assertEquals(300d, records.get(0).getMinutesWorked(), 1d);
        assertEquals(format.parse("29.10.2015"), records.get(0).getDate());
    }

    @Test
    public void testGetSkippedDataSets() throws Exception {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        importer.importFile(new ImportFile("file.xslx", importer.getExampleFile().getInputStream()));
        List<List<String>> skippedRecords = importer.getSkippedRecords();
        assertEquals(6, skippedRecords.size());

        assertEquals("file.xslx", skippedRecords.get(1).get(0));
    }

    @Test
    public void testGetExampleFile(){
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        ExampleFile file = importer.getExampleFile();
        assertNotNull(file.getFileName());
        assertNotNull(file.getInputStream());
        assertNotNull(file.getContentType());
    }

    @Test
    public void testValidity() throws IOException {
        UBWWorkRecordsImporter importer = new UBWWorkRecordsImporter();
        Workbook workbook = new XSSFWorkbook(importer.getExampleFile().getInputStream());
        assertTrue(importer.checkValidity(workbook));
    }
}
