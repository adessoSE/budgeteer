package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.imports.api.Importer;

public class ImporterChoiceRenderer implements IChoiceRenderer<Importer> {

    @Override
    public Object getDisplayValue(Importer object) {
        return object.getDisplayName();
    }

    @Override
    public String getIdValue(Importer object, int index) {
        return String.valueOf(index);
    }
}
