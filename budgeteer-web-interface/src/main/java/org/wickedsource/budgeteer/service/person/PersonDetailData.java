package org.wickedsource.budgeteer.service.person;

import org.joda.money.Money;

import java.util.Date;

public class PersonDetailData {

    private String name;

    private Money averageDailyRate;

    private Date firstBookedDate;

    private Date lastBookedDate;

    private Double hoursBooked;

    private Money budgetBurned;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getAverageDailyRate() {
        return averageDailyRate;
    }

    public void setAverageDailyRate(Money averageDailyRate) {
        this.averageDailyRate = averageDailyRate;
    }

    public Date getFirstBookedDate() {
        return firstBookedDate;
    }

    public void setFirstBookedDate(Date firstBookedDate) {
        this.firstBookedDate = firstBookedDate;
    }

    public Date getLastBookedDate() {
        return lastBookedDate;
    }

    public void setLastBookedDate(Date lastBookedDate) {
        this.lastBookedDate = lastBookedDate;
    }

    public Double getHoursBooked() {
        return hoursBooked;
    }

    public void setHoursBooked(Double hoursBooked) {
        this.hoursBooked = hoursBooked;
    }

    public Money getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Money budgetBurned) {
        this.budgetBurned = budgetBurned;
    }
}
