package org.wickedsource.budgeteer.persistence.contract;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractStatisticBean {

    private int year;


    /**
     * budgets are in cents
     */
    private long remainingContractBudget;
    private long spendBudget;
    private long invoicedBudget;

    /**
     * Month is 0-based;
     */
    private int month;

}
