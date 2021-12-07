package de.adesso.budgeteer.rest.project.exceptions;

import de.adesso.budgeteer.core.project.ProjectException;

public class CreateProjectException extends RestProjectException {
    public CreateProjectException(ProjectException projectException) {
        super(projectException);
    }
}
