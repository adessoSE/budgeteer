package org.wickedsource.budgeteer.imports.api;

import java.io.Serializable;
import java.util.Date;

public class ImportedWorkRecord implements Serializable {

    private String budgetName;

    private String personName;

    private Date date;

    private int minutesWorked;

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

    public int getMinutesWorked() {
        return minutesWorked;
    }

    public void setMinutesWorked(int minutesWorked) {
        this.minutesWorked = minutesWorked;
    }
}
