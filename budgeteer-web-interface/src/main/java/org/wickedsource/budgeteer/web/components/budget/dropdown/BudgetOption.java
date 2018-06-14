package org.wickedsource.budgeteer.web.components.budget.dropdown;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
class BudgetOption implements Serializable {
    private long id;
    private String name;
}
