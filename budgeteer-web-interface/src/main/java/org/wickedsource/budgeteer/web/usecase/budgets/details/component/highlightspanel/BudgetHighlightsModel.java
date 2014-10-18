package org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;

public class BudgetHighlightsModel extends LoadableDetachableModel<BudgetDetailData> implements IObjectClassAwareModel<BudgetDetailData> {

    @SpringBean
    private BudgetService service;

    private long budgetId;

    public BudgetHighlightsModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected BudgetDetailData load() {
        return service.loadBudgetDetailData(budgetId);
    }

    @Override
    public Class getObjectClass() {
        return BudgetDetailData.class;
    }
}
