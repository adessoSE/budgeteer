package org.wickedsource.budgeteer.service.record;

import org.joda.money.Money;

import java.util.Date;

public class AggregatedWorkingRecord {

    private String aggregationPeriodTitle;

    private Date aggregationPeriodStart;

    private Date aggregationPeriodEnd;

    private Double hours;

    private Money budgetPlanned;

    private Money budgetBurned;

    public String getAggregationPeriodTitle() {
        return aggregationPeriodTitle;
    }

    public void setAggregationPeriodTitle(String aggregationPeriodTitle) {
        this.aggregationPeriodTitle = aggregationPeriodTitle;
    }

    public Date getAggregationPeriodStart() {
        return aggregationPeriodStart;
    }

    public void setAggregationPeriodStart(Date aggregationPeriodStart) {
        this.aggregationPeriodStart = aggregationPeriodStart;
    }

    public Date getAggregationPeriodEnd() {
        return aggregationPeriodEnd;
    }

    public void setAggregationPeriodEnd(Date aggregationPeriodEnd) {
        this.aggregationPeriodEnd = aggregationPeriodEnd;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Money getBudgetPlanned() {
        return budgetPlanned;
    }

    public void setBudgetPlanned(Money budgetPlanned) {
        this.budgetPlanned = budgetPlanned;
    }

    public Money getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Money budgetBurned) {
        this.budgetBurned = budgetBurned;
    }

    public Money getDifference() {
        return budgetPlanned.minus(budgetBurned);
    }
}
