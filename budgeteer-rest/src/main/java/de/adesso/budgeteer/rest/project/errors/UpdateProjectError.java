package de.adesso.budgeteer.rest.project.errors;

import lombok.Data;

@Data
public class UpdateProjectError {
    private boolean nameAlreadyInUse;
    private boolean projectNotFound;
}
