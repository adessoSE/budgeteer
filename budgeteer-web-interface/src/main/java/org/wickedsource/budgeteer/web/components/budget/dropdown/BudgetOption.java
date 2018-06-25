package org.wickedsource.budgeteer.web.components.budget.dropdown;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class BudgetOption implements Serializable {
	private long id;
	private String name;
}
