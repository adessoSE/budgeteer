package org.wickedsource.budgeteer.service.record;

import java.io.Serializable;
import java.util.Date;

public class SingleRecord implements Serializable {

    private String budgetName;

    private String personName;

    private Double dailyRate;

    private Date date;

    private Double hours;

    private Double budgetBurned;

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

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
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

    public Double getBudgetBurned() {
        return budgetBurned;
    }

    public void setBudgetBurned(Double budgetBurned) {
        this.budgetBurned = budgetBurned;
    }
}
