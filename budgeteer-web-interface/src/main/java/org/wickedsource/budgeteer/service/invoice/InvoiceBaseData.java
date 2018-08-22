package org.wickedsource.budgeteer.service.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.components.fileUpload.FileUploadModel;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceBaseData implements Serializable {
    private long invoiceId;
    private long contractId;
    private String contractName;
    private String invoiceName;
    private Money sum;
    private String internalNumber;
    private int year;
    private int month;
    private Date paidDate;
    private Date dueDate;

    private FileUploadModel fileUploadModel = new FileUploadModel();

    private List<DynamicAttributeField> dynamicInvoiceFields;

    public InvoiceBaseData(long contractId, String contractName){
        this.contractId = contractId;
        this.contractName = contractName;
        dynamicInvoiceFields = new LinkedList<>();
    }

    public boolean isPaid(){
        return paidDate != null && paidDate != new Date(0);
    }
}
