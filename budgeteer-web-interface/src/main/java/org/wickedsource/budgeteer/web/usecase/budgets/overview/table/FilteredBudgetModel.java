package org.wickedsource.budgeteer.web.usecase.budgets.overview.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.util.List;

public class FilteredBudgetModel extends LoadableDetachableModel<List<BudgetDetailData>> {

    @SpringBean
    private BudgetService service;

    private long userId;

    private IModel<BudgetTagFilter> filterModel;

    public FilteredBudgetModel(long userId, IModel<BudgetTagFilter> filterModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
        this.userId = userId;
    }

    @Override
    protected List<BudgetDetailData> load() {
        return service.loadBudgetsDetailData(userId, filterModel.getObject());
    }

    public void setFilter(IModel<BudgetTagFilter> filterModel) {
        this.filterModel = filterModel;
    }
}
