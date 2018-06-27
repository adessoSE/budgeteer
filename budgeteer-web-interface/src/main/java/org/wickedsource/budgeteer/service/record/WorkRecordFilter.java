package org.wickedsource.budgeteer.service.record;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

public class WorkRecordFilter implements Serializable {

	@Getter
	@Setter
	private long projectId;

	@Getter
	private List<PersonBaseData> personList = new LinkedList<>();

	@Getter
	private List<PersonBaseData> possiblePersons = new LinkedList<>();

	@Getter
	private List<BudgetBaseData> budgetList = new LinkedList<>();

	@Getter
	private List<BudgetBaseData> possibleBudgets = new LinkedList<>();

	@Getter
	@Setter
	private DateRange dateRange;

	public WorkRecordFilter(long projectId) {
		this.projectId = projectId;
	}
}
