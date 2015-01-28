package org.wickedsource.budgeteer.persistence.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissingBudgetTotalBean {

    private long budgetId;
    private String budgetName;
}
