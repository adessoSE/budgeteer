package org.wickedsource.budgeteer.importer.resourceplan;

import org.joda.money.CurrencyUnit;
import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.ImportedPlanRecord;

import java.util.List;

public class ResourcePlanImporterTest {

    @Test
    public void testImportFile() throws Exception {
        ResourcePlanImporter importer = new ResourcePlanImporter();
        ImportFile file = new ImportFile("resource_plan.xlsx", getClass().getResourceAsStream("/resource_plan.xlsx"));
        List<ImportedPlanRecord> records = importer.importFile(file, CurrencyUnit.EUR);

        Assert.assertEquals(26, records.size());

        Assert.assertEquals(480, records.get(2).getMinutesPlanned());
        Assert.assertEquals("Tom", records.get(2).getPersonName());
        Assert.assertEquals("Budget 1", records.get(2).getBudgetName());
        Assert.assertEquals(50000, records.get(2).getDailyRate().getAmountMinorInt());

        Assert.assertEquals(240, records.get(11).getMinutesPlanned());
        Assert.assertEquals("Tom", records.get(11).getPersonName());
        Assert.assertEquals("Budget 1", records.get(11).getBudgetName());
        Assert.assertEquals(50000, records.get(11).getDailyRate().getAmountMinorInt());

        Assert.assertEquals(480, records.get(22).getMinutesPlanned());
        Assert.assertEquals("Tom", records.get(22).getPersonName());
        Assert.assertEquals("Budget 2", records.get(22).getBudgetName());
        Assert.assertEquals(100000, records.get(22).getDailyRate().getAmountMinorInt());
    }

    @Test
    public void testGetExampleFile(){
        ResourcePlanImporter importer = new ResourcePlanImporter();
        ExampleFile file = importer.getExampleFile();
        Assert.assertNotNull(file.getFileName());
        Assert.assertNotNull(file.getInputStream());
        Assert.assertNotNull(file.getContentType());
    }
}