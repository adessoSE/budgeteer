package org.wickedsource.budgeteer.aproda;

import org.wickedsource.budgeteer.imports.api.WorkingRecord;
import org.wickedsource.budgeteer.imports.api.WorkingRecordsImporter;

import java.util.Arrays;
import java.util.List;

public class AprodaWorkingRecordsImporter implements WorkingRecordsImporter {

    @Override
    public List<WorkingRecord> importFile(byte[] file) {
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
