package org.wickedsource.budgeteer.persistence.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LimitReachedBean {

    private long budgetId;
    private String budgetName;
}