package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.contract.ContractTotalData;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.pages.budgets.overview.table.TotalBudgetDetailsModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class TotalContractDetailsModel extends LoadableDetachableModel<ContractTotalData> {
    private IModel<List<ContractTotalData>> wrappedModel;

    public TotalContractDetailsModel(IModel<List<ContractBaseData>> sourceModel) {
        List<ContractTotalData> totalDataList = new ArrayList<>();

        // Cast given sourceModel to a model of ContractTotalData.
        for (ContractBaseData baseData : sourceModel.getObject()) {
            totalDataList.add(new ContractTotalData(baseData));
        }

        wrappedModel = model(from(totalDataList));
    }

    /**
     * @return Size of the contract attributes in each contract
     */
    public int getContractAttributeSize()

    {
        List<DynamicAttributeField> fields = new ArrayList<DynamicAttributeField>();
        if (wrappedModel.getObject().size() > 0) {
            ContractTotalData data = wrappedModel.getObject().get(0);
            fields = data.getContractAttributes();
        }
        return fields.size();
    }

    @Override
    protected ContractTotalData load() {
        ContractTotalData totalData = new ContractTotalData();

        totalData.setBudgetLeft(MoneyUtil.createMoney(0d));
        totalData.setBudget(MoneyUtil.createMoney(0d));
        totalData.setBudgetSpent(MoneyUtil.createMoney(0d));

        totalData.setBudgetGross(MoneyUtil.createMoney(0d));
        totalData.setBudgetLeftGross(MoneyUtil.createMoney(0d));
        totalData.setBudgetSpentGross(MoneyUtil.createMoney(0d));

        // Sum up the money amounts for all contracts with and without taxes
        for (ContractTotalData single : wrappedModel.getObject()) {
            totalData.setBudgetSpent(totalData.getBudgetSpent().plus(single.getBudgetSpent()));
            totalData.setBudget(totalData.getBudget().plus(single.getBudget()));
            totalData.setBudgetLeft(totalData.getBudgetLeft().plus(single.getBudgetLeft()));

            totalData.setBudgetGross(totalData.getBudgetGross().plus(MoneyUtil.getMoneyWithTaxes(single.getBudget(), BigDecimal.valueOf(single.getTaxRate()))));
            totalData.setBudgetLeftGross(totalData.getBudgetLeftGross().plus(MoneyUtil.getMoneyWithTaxes(single.getBudgetLeft(), BigDecimal.valueOf(single.getTaxRate()))));
            totalData.setBudgetSpentGross(totalData.getBudgetSpentGross().plus(MoneyUtil.getMoneyWithTaxes(single.getBudgetSpent(), BigDecimal.valueOf(single.getTaxRate()))));
        }

        return totalData;
    }
}