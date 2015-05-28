package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractBaseData implements Serializable{
    private long contractId;
    private long projectId;
    private String contractName;
    private Money budget;
    private String internalNumber;
    private int year;
    private ContractEntity.ContractType type;
    private List<ContractFieldData> contractAttributes;

    private List<BudgetBaseData> belongingBudgets;

    public ContractBaseData(long projectId){
        this.projectId = projectId;
        contractAttributes = new LinkedList<ContractFieldData>();
    }
}
