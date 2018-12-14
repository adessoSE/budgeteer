package de.adesso.budgeteer.web.pages.budgets;

import de.adesso.budgeteer.service.budget.BudgetService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
