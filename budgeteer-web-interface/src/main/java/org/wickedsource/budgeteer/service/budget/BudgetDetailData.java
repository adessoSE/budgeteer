package org.wickedsource.budgeteer.service.budget;

import org.joda.money.Money;

import java.util.Date;
import java.util.List;

public class BudgetDetailData {

    private long id;

    private String name;

    private List<String> tags;

    private Money total;

    private Money spent;

    private Date lastUpdated;

    private Money avgDailyRate;

    private Money unplanned;

    public BudgetDetailData(){

    }

    public Money getUnplanned() {
        return unplanned;
    }

    public void setUnplanned(Money unplanned) {
        this.unplanned = unplanned;
    }

    public Money getAvgDailyRate() {
        return avgDailyRate;
    }

    public void setAvgDailyRate(Money avgDailyRate) {
        this.avgDailyRate = avgDailyRate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public Money getSpent() {
        return spent;
    }

    public void setSpent(Money spent) {
        this.spent = spent;
    }

    public Money getRemaining() {
        return this.total.minus(this.spent);
    }

    public Double getProgress() {
        return this.getRemaining().getAmount().doubleValue() / this.total.getAmount().doubleValue();
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
