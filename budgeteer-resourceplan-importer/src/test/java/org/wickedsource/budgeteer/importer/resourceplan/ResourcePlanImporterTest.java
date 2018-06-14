package org.wickedsource.budgeteer.importer.resourceplan;

import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.imports.api.*;

import java.util.List;

class ResourcePlanImporterTest {

    @Test
    void testImportFile() throws Exception {
        ResourcePlanImporter importer = new ResourcePlanImporter();
        ImportFile file = new ImportFile("resource_plan.xlsx", getClass().getResourceAsStream("/resource_plan.xlsx"));
        List<ImportedPlanRecord> records = importer.importFile(file, CurrencyUnit.EUR);

        Assertions.assertEquals(26, records.size());

        Assertions.assertEquals(480, records.get(2).getMinutesPlanned());
        Assertions.assertEquals("Tom", records.get(2).getPersonName());
        Assertions.assertEquals("Budget 1", records.get(2).getBudgetName());
        Assertions.assertEquals(50000, records.get(2).getDailyRate().getAmountMinorInt());

        Assertions.assertEquals(240, records.get(11).getMinutesPlanned());
        Assertions.assertEquals("Tom", records.get(11).getPersonName());
        Assertions.assertEquals("Budget 1", records.get(11).getBudgetName());
        Assertions.assertEquals(50000, records.get(11).getDailyRate().getAmountMinorInt());

        Assertions.assertEquals(480, records.get(22).getMinutesPlanned());
        Assertions.assertEquals("Tom", records.get(22).getPersonName());
        Assertions.assertEquals("Budget 2", records.get(22).getBudgetName());
        Assertions.assertEquals(100000, records.get(22).getDailyRate().getAmountMinorInt());
    }

    @Test
    void testGetExampleFile(){
        ResourcePlanImporter importer = new ResourcePlanImporter();
        ExampleFile file = importer.getExampleFile();
        Assertions.assertNotNull(file.getFileName());
        Assertions.assertNotNull(file.getInputStream());
        Assertions.assertNotNull(file.getContentType());
    }

    @Test
    void testInvalidFile() {
        Assertions.assertThrows(InvalidFileFormatException.class, () -> {
            ResourcePlanImporter importer = new ResourcePlanImporter();
            ImportFile file = new ImportFile("resource_plan_invalid.xlsx", getClass().getResourceAsStream("/resource_plan_invalid.xlsx"));
            importer.importFile(file, CurrencyUnit.EUR);
        });
    }
}