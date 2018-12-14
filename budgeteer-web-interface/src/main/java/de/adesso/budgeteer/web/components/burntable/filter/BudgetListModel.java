package de.adesso.budgeteer.web.components.burntable.filter;

import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.service.budget.BudgetService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class BudgetListModel extends LoadableDetachableModel<List<BudgetBaseData>> {

    @SpringBean
    private BudgetService service;

    private long projectId;

    public BudgetListModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    protected List<BudgetBaseData> load() {
        return service.loadBudgetBaseDataForProject(projectId);
    }
}
