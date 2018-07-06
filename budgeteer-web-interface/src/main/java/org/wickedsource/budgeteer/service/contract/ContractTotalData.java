package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.money.Money;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTotalData extends ContractBaseData {
    private Money budgetSpentGross;
    private Money budgetGross;
    private Money budgetLeftGross;

    public ContractTotalData(ContractBaseData baseData) {
        super();
        super.setContractAttributes(baseData.getContractAttributes());
        super.setBudget(baseData.getBudget());
        super.setBudgetLeft(baseData.getBudgetLeft());
        super.setBudgetSpent(baseData.getBudgetSpent());
        super.setTaxRate(baseData.getTaxRate());
    }
}
