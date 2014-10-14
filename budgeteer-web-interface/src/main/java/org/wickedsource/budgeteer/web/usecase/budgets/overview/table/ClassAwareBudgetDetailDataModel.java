package org.wickedsource.budgeteer.web.usecase.budgets.overview.table;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;

public class ClassAwareBudgetDetailDataModel implements IObjectClassAwareModel<BudgetDetailData> {

    private IModel<BudgetDetailData> wrappedModel;

    public ClassAwareBudgetDetailDataModel(IModel<BudgetDetailData> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }


    @Override
    public Class<BudgetDetailData> getObjectClass() {
        return BudgetDetailData.class;
    }

    @Override
    public BudgetDetailData getObject() {
        return wrappedModel.getObject();
    }

    @Override
    public void setObject(BudgetDetailData object) {
        wrappedModel.setObject(object);
    }

    @Override
    public void detach() {
        wrappedModel.detach();
    }
}
