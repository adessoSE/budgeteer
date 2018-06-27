package org.wickedsource.budgeteer.web.components.tax;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class TaxSwitchLabelModel<T> extends AbstractReadOnlyModel<T> {

	private IModel<T> netModel;
	private IModel<T> grossModel;

	public TaxSwitchLabelModel(T netValue, T grossValue) {
		this.netModel = Model.of(netModel);
		this.grossModel = Model.of(grossModel);
	}

	public TaxSwitchLabelModel(IModel<T> netModel, IModel<T> grossModel) {
		this.netModel = netModel;
		this.grossModel = grossModel;
	}

	@Override
	public T getObject() {
		if (BudgeteerSession.get().isTaxEnabled()) {
			return grossModel.getObject();
		} else {
			return netModel.getObject();
		}
	}

	@Override
	public void detach() {
		netModel.detach();
		grossModel.detach();
	}

}
