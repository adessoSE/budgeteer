package org.wickedsource.budgeteer.persistence.person;

import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

public class PersonBaseDataBean implements Serializable {

    private Long id;

    private String name;

    private Long averageDailyRateInCents;

    private Money defaultDailyRate;

    private Date lastBookedDate;

    public PersonBaseDataBean(Long id, String name, Long valuedMinutes, Long valuedRate, Date lastBookedDate, Money defaultDailyRate) {
        this.id = id;
        this.name = name;
        if (valuedRate == null || valuedRate == 0L)
            this.averageDailyRateInCents = 0L;
        else
            this.averageDailyRateInCents = valuedMinutes / valuedRate;
        this.lastBookedDate = lastBookedDate;
        this.defaultDailyRate = defaultDailyRate;
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

    public Date getLastBookedDate() {
        return lastBookedDate;
    }

    public Money getDefaultDailyRate(){
        return defaultDailyRate;
    }
}
