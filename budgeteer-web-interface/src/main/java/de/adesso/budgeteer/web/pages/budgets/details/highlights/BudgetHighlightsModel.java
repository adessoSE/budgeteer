package de.adesso.budgeteer.web.pages.budgets.details.highlights;

import de.adesso.budgeteer.service.budget.BudgetDetailData;
import de.adesso.budgeteer.service.budget.BudgetService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
