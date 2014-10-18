package org.wickedsource.budgeteer.web.pages.budgets.components.weekreporttable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.record.RecordService;

import java.util.List;

public class MultiBudgetsMonthlyAggregatedRecordsModel extends LoadableDetachableModel<List<AggregatedRecord>> {

    @SpringBean
    private RecordService service;

    private long budgetId;

    private IModel<BudgetTagFilter> filterModel;

    public MultiBudgetsMonthlyAggregatedRecordsModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    public MultiBudgetsMonthlyAggregatedRecordsModel(IModel<BudgetTagFilter> filterModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
    }

    @Override
    protected List<AggregatedRecord> load() {
        if (budgetId != 0) {
            return service.getMonthlyAggregationForBudget(budgetId);
        } else if (filterModel != null && filterModel.getObject() != null) {
            return service.getMonthlyAggregationForBudgets(filterModel.getObject());
        } else {
            throw new IllegalStateException("Neither budgetId nor filter specified. Specify at least one of these attributes in the constructor!");
        }
    }
}
