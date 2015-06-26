package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;

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
    private List<DynamicAttributeField> contractAttributes;
    private String link;
    private byte[] file;
    private String fileName;
    private List<InvoiceBaseData> invoices;

    private List<BudgetBaseData> belongingBudgets;

    public ContractBaseData(long projectId){
        this.projectId = projectId;
        contractAttributes = new LinkedList<DynamicAttributeField>();
    }
}
