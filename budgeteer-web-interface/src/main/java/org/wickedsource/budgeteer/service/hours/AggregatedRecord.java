package org.wickedsource.budgeteer.service.hours;

import java.util.Date;

public class AggregatedRecord {

    private String aggregationPeriodTitle;

    private Date aggregationPeriodStart;

    private Date aggregationPeriodEnd;

    private Double hours;

    private Double budgetPlanned;

    private Double budgetBurned;

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

    public Double getBudgetPlanned() {
        return budgetPlanned;
    }

    public void setBudgetPlanned(Double budgetPlanned) {
        this.budgetPlanned = budgetPlanned;
    }

    public Double getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Double budgetBurned) {
        this.budgetBurned = budgetBurned;
    }

    public Double getDifference() {
        return budgetPlanned - budgetBurned;
    }
}
