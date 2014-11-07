package org.wickedsource.budgeteer.persistence.person;

import java.io.Serializable;
import java.util.Date;

public class PersonBaseDataBean implements Serializable {

    private long id;

    private String name;

    private long averageDailyRateInCents;

    private Date lastBookedDate;

    public PersonBaseDataBean(long id, String name, long averageDailyRateInCents, Date lastBookedDate) {
        this.id = id;
        this.name = name;
        this.averageDailyRateInCents = averageDailyRateInCents;
        this.lastBookedDate = lastBookedDate;
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

    public Date getLastBookedDate() {
        return lastBookedDate;
    }
}
