package de.adesso.budgeteer.rest.project.exceptions;

import de.adesso.budgeteer.core.project.ProjectException;

public class UpdateProjectException extends RestProjectException {
    public UpdateProjectException(ProjectException projectException) {
        super(projectException);
    }
}
