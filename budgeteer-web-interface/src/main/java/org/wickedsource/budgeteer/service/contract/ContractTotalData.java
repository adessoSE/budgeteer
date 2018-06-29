package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;


@AllArgsConstructor
@NoArgsConstructor
public class ContractTotalData extends ContractBaseData {
    private Money budgetSpentGross;
    private Money budgetGross;
    private Money budgetLeftGross;

    public ContractTotalData(ContractBaseData baseData)
    {
        super();
        super.setContractAttributes(baseData.getContractAttributes());
        super.setBudget(baseData.getBudget());
        super.setBudgetLeft(baseData.getBudgetLeft());
        super.setBudgetSpent(baseData.getBudgetSpent());
        super.setTaxRate(baseData.getTaxRate());
    }

    public void setBudgetSpentGross(Money budget) {
        budgetSpentGross = budget;
    }

    public void setBudgetGross(Money budget) {
        budgetGross = budget;
    }

    public void setBudgetLeftGross(Money budget) {
        budgetLeftGross = budget;
    }

    public Money getBudgetSpentGross() {
        return budgetSpentGross;
    }

    public Money getBudgetGross() {
        return budgetGross;
    }

    public Money getBudgetLeftGross() {
        return budgetLeftGross;
    }
}
