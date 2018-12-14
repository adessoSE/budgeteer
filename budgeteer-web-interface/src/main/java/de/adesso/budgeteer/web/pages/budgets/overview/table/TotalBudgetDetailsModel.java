package de.adesso.budgeteer.web.pages.budgets.overview.table;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.service.budget.BudgetDetailData;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;

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
        totalData.setTotal_gross(MoneyUtil.createMoney(0d));
        totalData.setSpent(MoneyUtil.createMoney(0d));
        totalData.setSpent_gross(MoneyUtil.createMoney(0d));
        totalData.setUnplanned(MoneyUtil.createMoney(0d));
        totalData.setUnplanned_gross(MoneyUtil.createMoney(0d));
        totalData.setLastUpdated(new Date(0));
        for (BudgetDetailData singleData : wrappedModel.getObject()) {
            totalData.setTotal(totalData.getTotal().plus(singleData.getTotal()));
            totalData.setTotal_gross(totalData.getTotal_gross().plus(singleData.getTotal_gross()));
            totalData.setSpent(totalData.getSpent().plus(singleData.getSpent()));
            totalData.setSpent_gross(totalData.getSpent_gross().plus(singleData.getSpent_gross()));
            totalData.setUnplanned(totalData.getUnplanned().plus(singleData.getUnplanned()));
            totalData.setUnplanned_gross(totalData.getUnplanned_gross().plus(singleData.getUnplanned_gross()));
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
