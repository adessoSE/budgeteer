package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MissingDailyRateForBudgetBean extends MissingDailyRateBean {

    @Getter @Setter
    private String budgetName;

    public MissingDailyRateForBudgetBean(long personId, String personName, Date startDate, Date endDate, String budgetName) {
        super(personId, personName, startDate, endDate);
        this.budgetName = budgetName;
    }
}
