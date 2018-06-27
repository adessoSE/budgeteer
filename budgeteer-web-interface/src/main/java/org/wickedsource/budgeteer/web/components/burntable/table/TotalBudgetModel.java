package org.wickedsource.budgeteer.web.components.burntable.table;

import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.record.WorkRecord;

public class TotalBudgetModel extends AbstractReadOnlyModel<Money> {

	private IModel<List<WorkRecord>> model;

	public TotalBudgetModel(IModel<List<WorkRecord>> model) {
		this.model = model;
	}

	@Override
	public Money getObject() {
		Money sum = MoneyUtil.createMoney(0d);
		for (WorkRecord record : model.getObject()) {
			sum = sum.plus(record.getBudgetBurned());
		}
		return sum;
	}

	@Override
	public void detach() {

	}
}
