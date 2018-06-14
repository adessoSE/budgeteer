package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.service.imports.ImportService;

import java.util.List;

public class ImportersListModel extends AbstractReadOnlyModel<List<? extends Importer>> {

    @SpringBean
    private ImportService service;

    private List<? extends Importer> importers;

    public ImportersListModel() {
        Injector.get().inject(this);
    }

    @Override
    public List<? extends Importer> getObject() {
        if(importers == null){
            importers = service.getAvailableImporters();
        }
        return importers;
    }
}
