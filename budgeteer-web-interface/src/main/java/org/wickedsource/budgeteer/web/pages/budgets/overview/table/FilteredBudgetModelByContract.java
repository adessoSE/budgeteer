package org.wickedsource.budgeteer.web.pages.budgets.overview.table;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;

public class FilteredBudgetModelByContract extends LoadableDetachableModel<List<BudgetDetailData>> {
	@SpringBean
	private BudgetService service;

	private long contractId;


	public FilteredBudgetModelByContract(long contractId) {
		Injector.get().inject(this);
		this.contractId = contractId;
	}

	@Override
	protected List<BudgetDetailData> load() {
		return service.loadBudgetByContract(contractId);
	}
}
