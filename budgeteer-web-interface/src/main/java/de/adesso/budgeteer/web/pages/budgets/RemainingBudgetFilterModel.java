package de.adesso.budgeteer.web.pages.budgets;

import de.adesso.budgeteer.service.budget.BudgetService;
import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RemainingBudgetFilterModel extends LoadableDetachableModel<Long> {

    @SpringBean
    private BudgetService service;

    private long projectId;

    public RemainingBudgetFilterModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    protected Long load() {
        return BudgeteerSession.get().getRemainingBudgetFilterValue();
    }
}
