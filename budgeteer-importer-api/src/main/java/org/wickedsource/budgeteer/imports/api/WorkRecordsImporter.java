package org.wickedsource.budgeteer.imports.api;

import java.util.List;

public interface WorkRecordsImporter extends Importer {

	List<ImportedWorkRecord> importFile(ImportFile file) throws ImportException, InvalidFileFormatException;

}
