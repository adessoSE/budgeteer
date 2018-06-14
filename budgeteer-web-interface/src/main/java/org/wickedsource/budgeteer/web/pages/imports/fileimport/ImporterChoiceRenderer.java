package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class ImporterChoiceRenderer extends AbstractChoiceRenderer<Importer> {

    @Override
    public Object getDisplayValue(Importer object) {
        return object.getDisplayName();
    }
}
