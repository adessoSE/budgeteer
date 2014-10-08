package org.wickedsource.budgeteer.service.people;

import java.util.Date;

public class PersonDetailData {

    private String name;

    private Double averageDailyRate;

    private Date firstBookedDate;

    private Date lastBookedDate;

    private Double hoursBooked;

    private Double budgetBurned;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAverageDailyRate() {
        return averageDailyRate;
    }

    public void setAverageDailyRate(Double averageDailyRate) {
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

    public Double getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Double budgetBurned) {
        this.budgetBurned = budgetBurned;
    }
}
