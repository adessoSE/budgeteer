package de.adesso.budgeteer.web.pages.budgets.components.weekreporttable;

import de.adesso.budgeteer.service.budget.BudgetTagFilter;
import de.adesso.budgeteer.service.record.AggregatedRecord;
import de.adesso.budgeteer.service.record.RecordService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class BudgetsWeeklyAggregatedRecordsModel extends LoadableDetachableModel<List<AggregatedRecord>> {

    @SpringBean
    private RecordService service;

    private long budgetId;

    private IModel<BudgetTagFilter> filterModel;

    public BudgetsWeeklyAggregatedRecordsModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    public BudgetsWeeklyAggregatedRecordsModel(IModel<BudgetTagFilter> filterModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
    }

    @Override
    protected List<AggregatedRecord> load() {
        if (budgetId != 0) {
            return service.getWeeklyAggregationForBudgetWithTax(budgetId);
        } else if (filterModel != null && filterModel.getObject() != null) {
            return service.getWeeklyAggregationForBudgetsWithTaxes(filterModel.getObject());
        } else {
            throw new IllegalStateException("Neither budgetId nor filter specified. Specify at least one of these attributes in the constructor!");
        }
    }
}
