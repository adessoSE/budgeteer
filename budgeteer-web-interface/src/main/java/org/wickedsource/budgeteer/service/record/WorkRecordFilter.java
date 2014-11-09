package org.wickedsource.budgeteer.service.record;

import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

import java.io.Serializable;

public class WorkRecordFilter implements Serializable{

    private long projectId;

    private PersonBaseData person;

    private BudgetBaseData budget;

    private DateRange dateRange;

    public WorkRecordFilter(long projectId) {
        this.projectId = projectId;
    }

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

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
