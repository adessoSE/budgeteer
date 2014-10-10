package org.wickedsource.budgeteer.service.budget;

import java.io.Serializable;

public class BudgetBaseData implements Serializable {

    private long id;

    private String name;

    public BudgetBaseData() {
    }

    public BudgetBaseData(long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
