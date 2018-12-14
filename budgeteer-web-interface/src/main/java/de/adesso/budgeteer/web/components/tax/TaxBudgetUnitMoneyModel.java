package de.adesso.budgeteer.web.components.tax;

import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;

public class TaxBudgetUnitMoneyModel extends AbstractReadOnlyModel<Money> {

    private IModel<Money> netModel;
    private IModel<Money> grossModel;

    public TaxBudgetUnitMoneyModel(IModel<Money> netModel, IModel<Money> grossModel) {
        this.netModel = netModel;
        this.grossModel = grossModel;
    }

    @Override
    public Money getObject() {
        if (BudgeteerSession.get().isTaxEnabled()) {
            return grossModel.getObject();
        } else {
            return netModel.getObject();
        }
    }
}
