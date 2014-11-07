package org.wickedsource.budgeteer.persistence.person;

import java.util.Date;

public class PersonDetailDataBean {

    private long id;

    private String name;

    private long averageDailyRateInCents;

    private Date firstBookedDate;

    private Date lastBookedDate;

    private Double hoursBooked;

    private long budgetBurnedInCents;

    public PersonDetailDataBean(long id, String name, long averageDailyRateInCents, Date firstBookedDate, Date lastBookedDate, Double hoursBooked, long budgetBurnedInCents) {
        this.id = id;
        this.name = name;
        this.averageDailyRateInCents = averageDailyRateInCents;
        this.firstBookedDate = firstBookedDate;
        this.lastBookedDate = lastBookedDate;
        this.hoursBooked = hoursBooked;
        this.budgetBurnedInCents = budgetBurnedInCents;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAverageDailyRateInCents() {
        return averageDailyRateInCents;
    }

    public Date getFirstBookedDate() {
        return firstBookedDate;
    }

    public Date getLastBookedDate() {
        return lastBookedDate;
    }

    public Double getHoursBooked() {
        return hoursBooked;
    }

    public long getBudgetBurnedInCents() {
        return budgetBurnedInCents;
    }
}
