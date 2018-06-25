package org.wickedsource.budgeteer.web.components.money;

import java.math.RoundingMode;

import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class BudgetUnitMoneyModel implements IModel<Money> {

	private IModel<Money> sourceModel;

	public BudgetUnitMoneyModel(IModel<Money> sourceModel) {
		this.sourceModel = sourceModel;
	}


	@Override
	public Money getObject() {
		if (sourceModel.getObject() == null) {
			return MoneyUtil.createMoney(0d);
		} else {
			return sourceModel.getObject().dividedBy(BudgeteerSession.get().getSelectedBudgetUnit(), RoundingMode.FLOOR);
		}
	}

	@Override
	public void setObject(Money object) {
		sourceModel.setObject(object);
	}

	@Override
	public void detach() {
		sourceModel.detach();
	}
}
