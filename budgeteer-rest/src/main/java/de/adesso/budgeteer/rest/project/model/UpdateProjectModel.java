package de.adesso.budgeteer.rest.project.model;

import de.adesso.budgeteer.common.date.DateRange;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProjectModel {
    @NotEmpty
    String name;
    DateRange dateRange;
}
