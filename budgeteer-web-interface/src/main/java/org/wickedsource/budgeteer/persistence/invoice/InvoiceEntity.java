package org.wickedsource.budgeteer.persistence.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {
    private long id;
    private ContractEntity contract;
    private String name;
    private Money sum;
    private String internalNumber;
    private int year;
    private int month;
    private boolean paid;
}
