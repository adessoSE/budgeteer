package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.service.imports.ImportsService;

import java.util.List;

public class ImportersModel extends LoadableDetachableModel<List<? extends Importer>> {

    @SpringBean
    private ImportsService service;

    public ImportersModel() {
        Injector.get().inject(this);
    }

    @Override
    protected List<? extends Importer> load() {
        return service.getAvailableImporters();
    }
}
