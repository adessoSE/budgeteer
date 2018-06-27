package org.wickedsource.budgeteer.web.components.burntable.filter;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;

public class BudgetListModel extends LoadableDetachableModel<List<BudgetBaseData>> {

	@SpringBean
	private BudgetService service;

	private long projectId;

	public BudgetListModel(long projectId) {
		Injector.get().inject(this);
		this.projectId = projectId;
	}

	@Override
	protected List<BudgetBaseData> load() {
		return service.loadBudgetBaseDataForProject(projectId);
	}
}
