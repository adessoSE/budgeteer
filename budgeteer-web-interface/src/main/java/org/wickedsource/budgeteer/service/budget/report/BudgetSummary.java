package org.wickedsource.budgeteer.service.budget.report;

import lombok.Data;

@Data
class BudgetSummary {
	BudgetSummary(String description) {
		this.name = description;
	}
	private String name;
}
