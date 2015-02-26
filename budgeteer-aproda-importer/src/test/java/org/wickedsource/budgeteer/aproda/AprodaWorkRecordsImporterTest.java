package org.wickedsource.budgeteer.aproda;

import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.ImportedWorkRecord;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AprodaWorkRecordsImporterTest {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    public void testRead() throws Exception {
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        InputStream in = getClass().getResourceAsStream("/aproda-testreport.xlsx");
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", in));
        Assert.assertEquals(8, records.size());
        Assert.assertEquals("Mustermann, Max", records.get(0).getPersonName());
        Assert.assertEquals("Budget", records.get(0).getBudgetName());
        Assert.assertEquals(540d, records.get(0).getMinutesWorked(), 1d);
        Assert.assertEquals(format.parse("06.10.2014"), records.get(0).getDate());
    }

    @Test
    public void testGetSkippedDataSets() throws Exception {
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        InputStream in = getClass().getResourceAsStream("/aproda-testreport.xlsx");
        List<ImportedWorkRecord> records = importer.importFile(new ImportFile("file.xslx", in));
        List<List<String>> skippedRecords = importer.getSkippedDataSets();
        Assert.assertEquals(26, skippedRecords.size());

        Assert.assertEquals("file.xslx", skippedRecords.get(0).get(0) );
        Assert.assertEquals("Mustermann, Max", skippedRecords.get(2).get(0));
        Assert.assertEquals("30-Okt-2014", skippedRecords.get(2).get(1));
        Assert.assertEquals("Dortmund", skippedRecords.get(2).get(2));
        Assert.assertEquals("Projektinfrastruktur", skippedRecords.get(2).get(3));
        Assert.assertEquals("Projektinfrastruktur", skippedRecords.get(2).get(4));
        Assert.assertEquals("Budget", skippedRecords.get(2).get(5));
        Assert.assertEquals("Erstellung Entwicklungs-VM", skippedRecords.get(2).get(6));
        Assert.assertEquals("9.0", skippedRecords.get(2).get(7));
        Assert.assertEquals("Nein", skippedRecords.get(2).get(8));
        Assert.assertEquals("studentische Hilfskraft", skippedRecords.get(2).get(9));

        Assert.assertEquals("", skippedRecords.get(21).get(0));
        Assert.assertEquals("", skippedRecords.get(21).get(1));
        Assert.assertEquals("", skippedRecords.get(21).get(2));
        Assert.assertEquals("", skippedRecords.get(21).get(3));
        Assert.assertEquals("8.0", skippedRecords.get(21).get(7));
        Assert.assertEquals("Ja", skippedRecords.get(21).get(8));
        Assert.assertEquals("studentische Hilfskraft", skippedRecords.get(22).get(9));
    }

    @Test
    public void testGetExampleFile(){
        AprodaWorkRecordsImporter importer = new AprodaWorkRecordsImporter();
        ExampleFile file = importer.getExampleFile();
        Assert.assertNotNull(file.getFileName());
        Assert.assertNotNull(file.getInputStream());
        Assert.assertNotNull(file.getContentType());
    }
}
