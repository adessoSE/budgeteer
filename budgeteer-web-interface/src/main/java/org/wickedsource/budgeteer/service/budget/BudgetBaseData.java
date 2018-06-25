package org.wickedsource.budgeteer.service.budget;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetBaseData implements Serializable {

	private long id;
	private String name;
}
