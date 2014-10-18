package org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;

/**
 * A model that loads a budget's name from the database.s
 */
public class BudgetNameModel extends LoadableDetachableModel<String> {

    @SpringBean
    private BudgetService service;

    private long budgetId;

    public BudgetNameModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected String load() {
        return service.loadBudgetBaseData(budgetId).getName();
    }
}
