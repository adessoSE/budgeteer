package org.wickedsource.budgeteer.aproda;

import org.junit.Assert;
import org.junit.Test;
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
}
