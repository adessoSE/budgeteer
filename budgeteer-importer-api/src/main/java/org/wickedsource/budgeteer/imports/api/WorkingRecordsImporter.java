package org.wickedsource.budgeteer.imports.api;

import java.util.List;

public interface WorkingRecordsImporter extends Importer{

    List<WorkingRecord> importFile(byte[] file);

}
