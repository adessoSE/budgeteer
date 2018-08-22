package org.wickedsource.budgeteer.web.pages.budgets.overview.table;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;

import java.util.Date;
import java.util.List;

public class TotalBudgetDetailsModel extends LoadableDetachableModel<BudgetDetailData> implements IObjectClassAwareModel<BudgetDetailData> {

    private IModel<List<BudgetDetailData>> wrappedModel;

    public TotalBudgetDetailsModel(IModel<List<BudgetDetailData>> sourceModel) {
        this.wrappedModel = sourceModel;
    }

    @Override
    protected BudgetDetailData load() {
        BudgetDetailData totalData = new BudgetDetailData();
        totalData.setTotal(MoneyUtil.createMoney(0d));
        totalData.setTotalGross(MoneyUtil.createMoney(0d));
        totalData.setSpent(MoneyUtil.createMoney(0d));
        totalData.setSpentGross(MoneyUtil.createMoney(0d));
        totalData.setUnplanned(MoneyUtil.createMoney(0d));
        totalData.setUnplannedGross(MoneyUtil.createMoney(0d));
        totalData.setLastUpdated(new Date(0));
        for (BudgetDetailData singleData : wrappedModel.getObject()) {
            totalData.setTotal(totalData.getTotal().plus(singleData.getTotal()));
            totalData.setTotalGross(totalData.getTotalGross().plus(singleData.getTotalGross()));
            totalData.setSpent(totalData.getSpent().plus(singleData.getSpent()));
            totalData.setSpentGross(totalData.getSpentGross().plus(singleData.getSpentGross()));
            totalData.setUnplanned(totalData.getUnplanned().plus(singleData.getUnplanned()));
            totalData.setUnplannedGross(totalData.getUnplannedGross().plus(singleData.getUnplannedGross()));
            if (singleData.getLastUpdated() != null && singleData.getLastUpdated().after(totalData.getLastUpdated())) {
                totalData.setLastUpdated(singleData.getLastUpdated());
            }
        }
        return totalData;
    }

    @Override
    public Class<BudgetDetailData> getObjectClass() {
        return BudgetDetailData.class;
    }
}
