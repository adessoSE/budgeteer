package org.wickedsource.budgeteer.web.component.burntable.table;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.record.SingleRecord;

import java.util.List;

public class TotalBudgetModel extends AbstractReadOnlyModel<Money> {

    private IModel<List<SingleRecord>> model;

    public TotalBudgetModel(IModel<List<SingleRecord>> model) {
        this.model = model;
    }

    @Override
    public Money getObject() {
        Money sum = MoneyUtil.createMoney(0d);
        for (SingleRecord record : model.getObject()) {
            sum = sum.plus(record.getBudgetBurned());
        }
        return sum;
    }

    @Override
    public void detach() {

    }
}
