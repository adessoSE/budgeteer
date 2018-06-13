package org.wickedsource.budgeteer.web.pages.budgets.overview.table;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

public class FilteredBudgetModel extends LoadableDetachableModel<List<BudgetDetailData>> {

	@SpringBean private BudgetService service;

	private long projectId;

	private IModel<BudgetTagFilter> filterModel;

	public FilteredBudgetModel(long projectId, IModel<BudgetTagFilter> filterModel) {
		Injector.get().inject(this);
		this.filterModel = filterModel;
		this.projectId = projectId;
	}

	@Override
	protected List<BudgetDetailData> load() {
		return service.loadBudgetsDetailData(projectId, filterModel.getObject());
	}

	public void setFilter(IModel<BudgetTagFilter> filterModel) {
		this.filterModel = filterModel;
	}
}
