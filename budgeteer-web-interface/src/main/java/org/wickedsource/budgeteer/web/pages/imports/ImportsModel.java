package org.wickedsource.budgeteer.web.pages.imports;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.imports.Import;
import org.wickedsource.budgeteer.service.imports.ImportsService;

import java.util.List;

public class ImportsModel extends LoadableDetachableModel<List<Import>> {

    @SpringBean
    private ImportsService service;

    private long userId;

    public ImportsModel(long userId) {
        this.userId = userId;
        Injector.get().inject(this);
    }

    @Override
    protected List<Import> load() {
        return service.loadImports(userId);
    }
}
