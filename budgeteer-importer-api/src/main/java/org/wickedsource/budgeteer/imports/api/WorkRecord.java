package org.wickedsource.budgeteer.imports.api;

import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

public class WorkRecord implements Serializable {

    private String budgetName;

    private String personName;

    private Money dailyRate;

    private Date date;

    private Double hours;

    private Money budgetBurned;

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

    public Money getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Money dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Money getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Money budgetBurned) {
        this.budgetBurned = budgetBurned;
    }
}
