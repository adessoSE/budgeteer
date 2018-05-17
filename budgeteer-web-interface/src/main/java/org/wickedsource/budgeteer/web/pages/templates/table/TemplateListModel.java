package org.wickedsource.budgeteer.web.pages.templates.table;

import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.pages.templates.TemplateFilter;

import java.util.List;

public class TemplateListModel extends LoadableDetachableModel<List<Template>> {

    @SpringBean
    private TemplateService service;

    @Getter
    @Setter
    private TemplateFilter filter;

    public TemplateListModel(TemplateFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
    }

    @Override
    protected List<Template> load() {
        return service.getFilteredTemplatesInProject(filter);
    }
}
