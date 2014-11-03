package org.wickedsource.budgeteer.imports.api;

import java.util.List;

public interface WorkRecordsImporter extends Importer{

    List<WorkRecord> importFile(byte[] file);

}
