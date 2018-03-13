package org.wickedsource.budgeteer.persistence.contract;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ContractStatisticBean implements Serializable {

    private int year;


    /**
     * budgets are in cents
     */
    private double progress;
    private long remainingContractBudget;
    private long spentBudget;
    private long invoicedBudget;

    /**
     * Month is 0-based;
     */
    private int month;

    public long getDifference(){
        return spentBudget - invoicedBudget;
    }

    public double getProgressInPercent() {
    	return progress*100;
    }
}
