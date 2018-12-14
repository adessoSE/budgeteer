package de.adesso.budgeteer.web.pages.imports.fileimport;

import de.adesso.budgeteer.imports.api.Importer;
import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class ImporterChoiceRenderer extends AbstractChoiceRenderer<Importer> {

    @Override
    public Object getDisplayValue(Importer object) {
        return object.getDisplayName();
    }
}
