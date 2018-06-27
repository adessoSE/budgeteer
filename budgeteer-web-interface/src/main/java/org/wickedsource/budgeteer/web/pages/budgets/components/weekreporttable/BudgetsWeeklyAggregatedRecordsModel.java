package org.wickedsource.budgeteer.web.pages.budgets.components.weekreporttable;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.record.RecordService;

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
			//return service.getWeeklyAggregationForBudget(budgetId);
		} else if (filterModel != null && filterModel.getObject() != null) {
			return service.getWeeklyAggregationForBudgetsWithTaxes(filterModel.getObject());
			//return service.getWeeklyAggregationForBudgets(filterModel.getObject());
		} else {
			throw new IllegalStateException("Neither budgetId nor filter specified. Specify at least one of these attributes in the constructor!");
		}
	}
}
