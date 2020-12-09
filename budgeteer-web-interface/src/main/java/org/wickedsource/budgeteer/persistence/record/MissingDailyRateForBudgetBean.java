package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class MissingDailyRateForBudgetBean extends MissingDailyRateBean implements Serializable {

    @Getter @Setter
    private String budgetName;
    @Getter @Setter
    private long budgetId;

    public MissingDailyRateForBudgetBean(long personId, String personName, Date startDate, Date endDate, String budgetName, long budgetId) {
        super(personId, personName, startDate, endDate);
        this.budgetName = budgetName;
        this.budgetId = budgetId;
    }
}
