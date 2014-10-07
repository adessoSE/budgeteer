package org.wickedsource.budgeteer.service.people;

import java.util.Date;

public class Person {

    private long id;

    private String name;

    private Double averageDailyRate;

    private Date lastBooked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getLastBooked() {
        return lastBooked;
    }

    public void setLastBooked(Date lastBooked) {
        this.lastBooked = lastBooked;
    }
}
