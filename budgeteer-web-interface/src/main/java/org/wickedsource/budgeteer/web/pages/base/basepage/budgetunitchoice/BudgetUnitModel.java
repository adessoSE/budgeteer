package org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;

import java.util.List;

public class BudgetUnitModel extends LoadableDetachableModel<List<Double>> {

    @SpringBean
    private BudgetService service;

    private long userId;

    public BudgetUnitModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    protected List<Double> load() {
        return service.loadBudgetUnits(userId);
    }


}
