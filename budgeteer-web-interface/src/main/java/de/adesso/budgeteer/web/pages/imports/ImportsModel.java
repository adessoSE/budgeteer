package de.adesso.budgeteer.web.pages.imports;

import de.adesso.budgeteer.service.imports.Import;
import de.adesso.budgeteer.service.imports.ImportService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ImportsModel extends LoadableDetachableModel<List<Import>> {

    @SpringBean
    private ImportService service;

    private long projectId;

    public ImportsModel(long projectId) {
        this.projectId = projectId;
        Injector.get().inject(this);
    }

    @Override
    protected List<Import> load() {
        return service.loadImports(projectId);
    }
}
