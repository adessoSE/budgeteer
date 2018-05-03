package org.wickedsource.budgeteer.service.imports;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImporterRegistryTest {

    @Test
    void testRegistry() {
        ImporterRegistry registry = new ImporterRegistry();
        Assertions.assertEquals(2, registry.getWorkingRecordsImporters().size());
    }
}
