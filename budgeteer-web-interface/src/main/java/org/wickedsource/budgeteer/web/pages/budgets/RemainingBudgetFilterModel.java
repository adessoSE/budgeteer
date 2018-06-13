package org.wickedsource.budgeteer.web.pages.budgets;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.BudgeteerSession;

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
        return BudgeteerSession.get().getBudgetRemainingFilter();
    }
}
