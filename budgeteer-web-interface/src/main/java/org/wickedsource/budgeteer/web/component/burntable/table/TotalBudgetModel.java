package org.wickedsource.budgeteer.web.component.burntable.table;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.record.SingleRecord;

import java.util.List;

public class TotalBudgetModel extends AbstractReadOnlyModel<Double> {

    private IModel<List<SingleRecord>> model;

    public TotalBudgetModel(IModel<List<SingleRecord>> model) {
        this.model = model;
    }

    @Override
    public Double getObject() {
        Double sum = 0d;
        for (SingleRecord record : model.getObject()) {
            sum += record.getBudgetBurned();
        }
        return sum;
    }

    @Override
    public void detach() {

    }
}
