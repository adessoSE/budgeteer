package org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;

public class BudgetUnitModel extends LoadableDetachableModel<List<Double>> {

	@SpringBean private BudgetService service;

	private long projectId;

	public BudgetUnitModel(long projectId) {
		Injector.get().inject(this);
		this.projectId = projectId;
	}

	@Override
	protected List<Double> load() {
		return service.loadBudgetUnits(projectId);
	}
}
