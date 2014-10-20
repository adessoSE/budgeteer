package org.wickedsource.budgeteer.web.pages.budgets;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;

import java.util.ArrayList;
import java.util.List;

public class BudgetTagsModel extends LoadableDetachableModel<List<String>> {

    @SpringBean
    private BudgetService service;

    private long userId;

    public BudgetTagsModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    protected List<String> load() {
        return new ArrayList<String>(service.loadBudgetTags(userId));
    }
}
