package org.wickedsource.budgeteer.imports.api;

import java.util.List;

import org.joda.money.CurrencyUnit;

public interface PlanRecordsImporter extends Importer {

	List<ImportedPlanRecord> importFile(ImportFile file, CurrencyUnit currencyUnit)
			throws ImportException, InvalidFileFormatException;
}
