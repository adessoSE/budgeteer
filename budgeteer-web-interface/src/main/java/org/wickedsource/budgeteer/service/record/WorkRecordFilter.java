package org.wickedsource.budgeteer.service.record;

import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class WorkRecordFilter implements Serializable{

    private long projectId;

    private List<PersonBaseData> personList = new LinkedList<PersonBaseData>();

    private List<BudgetBaseData> budgetList = new LinkedList<BudgetBaseData>();

    private DateRange dateRange;

    public WorkRecordFilter(long projectId) {
        this.projectId = projectId;
    }

    public List<PersonBaseData> getPersonList() {
        return personList;
    }

    public List<BudgetBaseData> getBudgetList() {
        return budgetList;
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
