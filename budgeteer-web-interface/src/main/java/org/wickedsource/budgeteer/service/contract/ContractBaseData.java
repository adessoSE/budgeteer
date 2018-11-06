package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.web.components.fileUpload.FileUploadModel;

import java.io.Serializable;
import java.util.Date;
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
    private Money budgetSpent;
    private Money budgetLeft;
    private double taxRate; 
    private String internalNumber;
    private Date startDate;
    private ContractEntity.ContractType type;
    private List<DynamicAttributeField> contractAttributes;
    private FileUploadModel fileModel = new FileUploadModel();
    private List<InvoiceBaseData> belongingInvoices;

    private List<BudgetBaseData> belongingBudgets;

    public ContractBaseData(long projectId){
        this.projectId = projectId;
        contractAttributes = new LinkedList<>();
    }
}
