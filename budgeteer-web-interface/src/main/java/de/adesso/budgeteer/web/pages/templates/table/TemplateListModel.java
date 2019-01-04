package de.adesso.budgeteer.web.pages.templates.table;

import de.adesso.budgeteer.service.template.Template;
import de.adesso.budgeteer.service.template.TemplateService;
import de.adesso.budgeteer.web.pages.templates.TemplateFilter;
import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
