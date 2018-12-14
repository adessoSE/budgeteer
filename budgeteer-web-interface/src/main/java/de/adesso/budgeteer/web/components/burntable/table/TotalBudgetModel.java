package de.adesso.budgeteer.web.components.burntable.table;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.service.record.WorkRecord;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;

import java.util.List;

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
