package org.wickedsource.budgeteer.aproda;

import org.wickedsource.budgeteer.imports.api.WorkRecord;
import org.wickedsource.budgeteer.imports.api.WorkRecordsImporter;

import java.util.Arrays;
import java.util.List;

public class AprodaWorkRecordsImporter implements WorkRecordsImporter {

    @Override
    public List<WorkRecord> importFile(byte[] file) {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Aproda Working Hours Importer";
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return Arrays.asList(".xlsx");
    }
}
