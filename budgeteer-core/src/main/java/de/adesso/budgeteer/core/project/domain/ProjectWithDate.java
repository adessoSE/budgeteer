package de.adesso.budgeteer.core.project.domain;

import de.adesso.budgeteer.common.date.DateRange;
import lombok.Value;

@Value
public class ProjectWithDate {
    long id;
    String name;
    DateRange dateRange;
}
