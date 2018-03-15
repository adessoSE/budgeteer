package org.wickedsource.budgeteer.web.components.money;

import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;

import java.math.RoundingMode;

public class BudgetUnitMoneyModel implements IModel<Money> {

    private IModel<Money> sourceModel;
    private double taxRate;

    public BudgetUnitMoneyModel(IModel<Money> sourceModel, double taxRate) {
        this.sourceModel = sourceModel;
        this.taxRate = taxRate;
    }


    @Override
    public Money getObject() {
        if (sourceModel.getObject() == null) {
            return MoneyUtil.createMoney(0d);
        } else {
            return sourceModel.getObject().dividedBy(BudgeteerSession.get().getSelectedBudgetUnit(), RoundingMode.FLOOR).multipliedBy(taxRate, RoundingMode.FLOOR);
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
