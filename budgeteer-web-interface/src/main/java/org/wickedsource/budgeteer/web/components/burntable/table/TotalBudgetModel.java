package org.wickedsource.budgeteer.web.components.burntable.table;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.imports.api.WorkingRecord;

import java.util.List;

public class TotalBudgetModel extends AbstractReadOnlyModel<Money> {

    private IModel<List<WorkingRecord>> model;

    public TotalBudgetModel(IModel<List<WorkingRecord>> model) {
        this.model = model;
    }

    @Override
    public Money getObject() {
        Money sum = MoneyUtil.createMoney(0d);
        for (WorkingRecord record : model.getObject()) {
            sum = sum.plus(record.getBudgetBurned());
        }
        return sum;
    }

    @Override
    public void detach() {

    }
}
