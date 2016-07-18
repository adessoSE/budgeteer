package org.wickedsource.budgeteer.service.record;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

@Getter
@Setter
public class WorkRecordFilter implements Serializable{

    private long projectId;

    private List<PersonBaseData> personList = new LinkedList<>();

    private List<PersonBaseData> possiblePersons = new LinkedList<>();

    private List<BudgetBaseData> budgetList = new LinkedList<>();

    private List<BudgetBaseData> possibleBudgets = new LinkedList<>();

    private DateRange dateRange;

    public WorkRecordFilter(long projectId) {
        this.projectId = projectId;
    }
}
