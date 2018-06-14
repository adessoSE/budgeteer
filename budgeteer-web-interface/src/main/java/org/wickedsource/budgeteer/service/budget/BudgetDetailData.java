package org.wickedsource.budgeteer.service.budget;

import lombok.Data;
import org.joda.money.Money;

import java.util.Date;
import java.util.List;

@Data
public class BudgetDetailData {

    private long id;
    private String name;
    private String description;
    private List<String> tags;
    private Money total;
    private Money total_gross;
    private Money spent;
    private Money spent_gross;
    private Date lastUpdated;
    private Money avgDailyRate;
    private Money avgDailyRate_gross;
    private Money unplanned;
    private Money unplanned_gross;
    private String contractName;
    private long contractId;


    public Money getRemaining() {
        return this.total.minus(this.spent);
    }

    public Money getRemaining_gross() {
        return this.total_gross.minus(this.spent_gross);
    }

    public Double getProgress() {
        return this.getSpent().getAmount().doubleValue() / this.total.getAmount().doubleValue();
    }

    /**
     * Gets the progress in percent rounded to two decimal places.
     *
     * @return the progress in percent.
     */
    public Double getProgressInPercent() {
        return getProgress() * 100;
    }
}
