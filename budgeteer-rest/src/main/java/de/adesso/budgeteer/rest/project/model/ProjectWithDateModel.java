package de.adesso.budgeteer.rest.project.model;

import de.adesso.budgeteer.common.date.DateRange;
import lombok.Value;

@Value
public class ProjectWithDateModel {
    long id;
    String name;
    DateRange dateRange;
}
