package org.wickedsource.budgeteer.service.imports;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImporterRegistryTest {

    @Test
    public void testRegistry() {
        ImporterRegistry registry = new ImporterRegistry();
        Assertions.assertEquals(2, registry.getWorkingRecordsImporters().size());
    }
}
