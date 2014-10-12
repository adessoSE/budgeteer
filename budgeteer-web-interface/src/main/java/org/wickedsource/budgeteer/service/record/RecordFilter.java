package org.wickedsource.budgeteer.service.record;

import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.common.DateRange;
import org.wickedsource.budgeteer.service.people.PersonBaseData;

import java.io.Serializable;

public class RecordFilter implements Serializable{

    private PersonBaseData person;

    private BudgetBaseData budget;

    private DateRange dateRange;

    public PersonBaseData getPerson() {
        return person;
    }

    public void setPerson(PersonBaseData person) {
        this.person = person;
    }

    public BudgetBaseData getBudget() {
        return budget;
    }

    public void setBudget(BudgetBaseData budget) {
        this.budget = budget;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}
