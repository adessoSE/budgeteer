package org.wickedsource.budgeteer.aproda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.*;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class AprodaWorkRecordsImporterTest {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    void testRead() throws Exception {
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        InputStream in = getClass().getResourceAsStream("/aproda-testreport.xlsx");
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", in));
        Assertions.assertEquals(8, records.size());
        Assertions.assertEquals("Mustermann, Max", records.get(0).getPersonName());
        Assertions.assertEquals("Budget", records.get(0).getBudgetName());
        Assertions.assertEquals(540d, records.get(0).getMinutesWorked(), 1d);
        Assertions.assertEquals(format.parse("06.10.2014"), records.get(0).getDate());
    }

    @Test
    void testGetSkippedDataSets() throws Exception {
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        InputStream in = getClass().getResourceAsStream("/aproda-testreport.xlsx");
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", in));
        List<List<String>> skippedRecords = importer.getSkippedRecords();
        Assertions.assertEquals(26, skippedRecords.size());

        Assertions.assertEquals("file.xslx", skippedRecords.get(1).get(0) );
        Assertions.assertEquals("Mustermann, Max", skippedRecords.get(2).get(0));
//        Assertions.assertEquals("30-Okt-2014", skippedRecords.get(2).get(1)); // don't check since locale is ignored
        Assertions.assertEquals("Dortmund", skippedRecords.get(2).get(2));
        Assertions.assertEquals("Projektinfrastruktur", skippedRecords.get(2).get(3));
        Assertions.assertEquals("Projektinfrastruktur", skippedRecords.get(2).get(4));
        Assertions.assertEquals("Budget", skippedRecords.get(2).get(5));
        Assertions.assertEquals("Erstellung Entwicklungs-VM", skippedRecords.get(2).get(6));
        Assertions.assertEquals("9.0", skippedRecords.get(2).get(7));
        Assertions.assertEquals("Nein", skippedRecords.get(2).get(8));
        Assertions.assertEquals("studentische Hilfskraft", skippedRecords.get(2).get(9));

        Assertions.assertEquals("", skippedRecords.get(21).get(0));
        Assertions.assertEquals("", skippedRecords.get(21).get(1));
        Assertions.assertEquals("", skippedRecords.get(21).get(2));
        Assertions.assertEquals("", skippedRecords.get(21).get(3));
        Assertions.assertEquals("8.0", skippedRecords.get(21).get(7));
        Assertions.assertEquals("Ja", skippedRecords.get(21).get(8));
        Assertions.assertEquals("studentische Hilfskraft", skippedRecords.get(22).get(9));
    }

    @Test
    void testGetExampleFile(){
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        ExampleFile file = importer.getExampleFile();
        Assertions.assertNotNull(file.getFileName());
        Assertions.assertNotNull(file.getInputStream());
        Assertions.assertNotNull(file.getContentType());
    }

    @Test
    void testInvalidFile() {
        Assertions.assertThrows(InvalidFileFormatException.class, () -> {
            AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
            InputStream in = getClass().getResourceAsStream("/aproda-testreport_invalid.xlsx");
            importer.importFile(new ImportFile("aproda-testreport_invalid.xslx", in));
        });
    }
}
