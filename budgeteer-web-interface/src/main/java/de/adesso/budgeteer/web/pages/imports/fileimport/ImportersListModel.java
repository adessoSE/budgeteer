package de.adesso.budgeteer.web.pages.imports.fileimport;

import de.adesso.budgeteer.imports.api.Importer;
import de.adesso.budgeteer.service.imports.ImportService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
