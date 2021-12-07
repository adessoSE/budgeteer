package de.adesso.budgeteer.rest.project.errors;

import lombok.Data;

@Data
public class CreateProjectError {
    private boolean namealreadyInUse;
}
