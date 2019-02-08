package org.wickedsource.budgeteer.persistence.person;

import java.util.Date;

public class PersonDetailDataBean {

    private Long id;

    private String name;

    private Long averageDailyRateInCents;

    private Date firstBookedDate;

    private Date lastBookedDate;

    private Double hoursBooked;

    private Long budgetBurnedInCents;

    public PersonDetailDataBean(Long id, String name, Long valuedMinutes, Long valuedRate, Date firstBookedDate, Date lastBookedDate, Double hoursBooked, Long budgetBurnedInCents) {
        this.id = id;
        this.name = name;
        this.firstBookedDate = firstBookedDate;
        this.lastBookedDate = lastBookedDate;

        //Set the rate
        if (valuedRate == null || valuedRate == 0L)
            this.averageDailyRateInCents = 0L;
        else
            this.averageDailyRateInCents = valuedMinutes / valuedRate;

        //Set the booked hours
        if (hoursBooked == null)
            this.hoursBooked = 0.0;
        else
            this.hoursBooked = hoursBooked;

        //Set the burned budget
        if (budgetBurnedInCents == null)
            this.budgetBurnedInCents = 0L;
        else
            this.budgetBurnedInCents = budgetBurnedInCents;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getAverageDailyRateInCents() {
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

    public Long getBudgetBurnedInCents() {
        return budgetBurnedInCents;
    }
}
