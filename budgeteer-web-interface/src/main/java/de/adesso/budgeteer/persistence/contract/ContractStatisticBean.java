package de.adesso.budgeteer.persistence.contract;

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
    private Double progress;
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

    public Double getProgressInPercent() {
        if (progress != null) {
            return progress * 100;
        } else {
            return null;
        }
    }
}
