package org.wickedsource.budgeteer.imports.api;

import java.io.Serializable;
import java.util.Date;

public class ImportedRecord implements Serializable {

    private String budgetName;

    private String personName;

    private Date date;

    public ImportedRecord() {}

    public ImportedRecord(String budgetName, String personName, Date date) {
        this.budgetName = budgetName;
        this.personName = personName;
        this.date = date;
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
