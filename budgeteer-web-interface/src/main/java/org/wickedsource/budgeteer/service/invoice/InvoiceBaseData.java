package org.wickedsource.budgeteer.service.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import java.io.Serializable;
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
    private boolean paid;

    private List<DynamicAttributeField> invoiceAttributes;

    public InvoiceBaseData(long contractId, String contractName){
        this.contractId = contractId;
        this.contractName = contractName;
        invoiceAttributes = new LinkedList<DynamicAttributeField>();
    }
}
