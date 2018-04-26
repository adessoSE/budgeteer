package org.wickedsource.budgeteer.web.components.templatesTable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;

import java.util.List;

public class TemplateListModel extends LoadableDetachableModel<List<Template>> {

    @SpringBean
    private TemplateService service;

    private long projectId;

    public TemplateListModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    protected List<Template> load() {
        return service.getTemplates();
    }
}
