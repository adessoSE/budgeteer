package de.adesso.budgeteer.rest.project.exceptions;

import de.adesso.budgeteer.core.project.ProjectException;

public abstract class RestProjectException extends RuntimeException {
    private final ProjectException projectException;

    protected RestProjectException(ProjectException projectException) {
        super(projectException);
        this.projectException = projectException;
    }

    public ProjectException getProjectException() {
        return projectException;
    }
}
