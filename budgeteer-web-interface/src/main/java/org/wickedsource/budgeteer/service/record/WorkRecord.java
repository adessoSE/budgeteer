package org.wickedsource.budgeteer.service.record;

import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

public class WorkRecord implements Serializable {

    private String budgetName;

    private String personName;

    private Date date;

    private double hours;

    private Money budgetBurned;

    private Money dailyRate;

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public Money getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Money budgetBurned) {
        this.budgetBurned = budgetBurned;
    }

    public Money getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Money dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}

