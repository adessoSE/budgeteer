package org.wickedsource.budgeteer.persistence.record;

import java.util.Date;

public class MissingDailyRateForBudgetBean extends MissingDailyRateBean {

    private String budgetName;

    public MissingDailyRateForBudgetBean(long personId, String personName, Date startDate, Date endDate, String budgetName) {
        super(personId, personName, startDate, endDate);
        this.budgetName = budgetName;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }
}
