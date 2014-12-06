package org.wickedsource.budgeteer.imports.api;

import org.joda.money.CurrencyUnit;

import java.util.List;

public interface PlanRecordsImporter extends Importer {

    List<ImportedPlanRecord> importFile(ImportFile file, CurrencyUnit currencyUnit) throws ImportException;

}
