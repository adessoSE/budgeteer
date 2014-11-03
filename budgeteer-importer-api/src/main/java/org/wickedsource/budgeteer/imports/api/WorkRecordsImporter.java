package org.wickedsource.budgeteer.imports.api;

import java.io.InputStream;
import java.util.List;

public interface WorkRecordsImporter extends Importer {

    List<ImportedWorkRecord> importFile(byte[] file) throws ImportException;

    List<ImportedWorkRecord> importFile(InputStream in) throws ImportException;

}
