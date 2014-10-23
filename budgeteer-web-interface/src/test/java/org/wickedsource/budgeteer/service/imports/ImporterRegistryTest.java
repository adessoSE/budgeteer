package org.wickedsource.budgeteer.service.imports;

import org.junit.Assert;
import org.junit.Test;

public class ImporterRegistryTest {

    @Test
    public void testRegistry() {
        ImporterRegistry registry = new ImporterRegistry();
        Assert.assertEquals(1, registry.getWorkingRecordsImporters().size());
    }
}
