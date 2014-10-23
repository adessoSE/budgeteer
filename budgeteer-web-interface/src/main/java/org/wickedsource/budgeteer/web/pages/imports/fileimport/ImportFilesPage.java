package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;

public class ImportFilesPage extends DialogPage {

    public ImportFilesPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        add(new DropDownChoice<Importer>("importerChoice", new ImportersModel(), new ImporterChoiceRenderer()));
    }

}
