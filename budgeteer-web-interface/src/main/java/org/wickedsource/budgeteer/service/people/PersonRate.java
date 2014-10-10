package org.wickedsource.budgeteer.service.people;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.io.Serializable;

public class PersonRate implements Serializable {

    private Double rate;

    private BudgetBaseData budget;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    private DateRange dateRange;

    public BudgetBaseData getBudget() {
        return budget;
    }

    public void setBudget(BudgetBaseData budget) {
        this.budget = budget;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static PersonRate copy(PersonRate rate){
        PersonRate copy = new PersonRate();
        copy.setDateRange(rate.getDateRange());
        copy.setRate(rate.getRate());
        copy.setBudget(rate.getBudget());
        return copy;
    }
}
