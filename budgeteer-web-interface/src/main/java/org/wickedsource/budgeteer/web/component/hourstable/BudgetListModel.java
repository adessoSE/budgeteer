package org.wickedsource.budgeteer.web.component.hourstable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;

import java.util.List;

public class BudgetListModel extends LoadableDetachableModel<List<BudgetBaseData>> {

    @SpringBean
    private BudgetService service;

    private long userId;

    public BudgetListModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    protected List<BudgetBaseData> load() {
        return service.loadBudgetBaseData(userId);
    }
}
