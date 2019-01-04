package de.adesso.budgeteer.service.contract;

import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.service.invoice.InvoiceBaseData;
import de.adesso.budgeteer.web.components.fileUpload.FileUploadModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractBaseData implements Serializable {
    private long contractId;
    private long projectId;
    private String contractName;
    private Integer sortingIndex;
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

    public ContractBaseData(long projectId) {
        this.projectId = projectId;
        contractAttributes = new LinkedList<>();
    }
}
