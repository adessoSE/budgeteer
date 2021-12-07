package de.adesso.budgeteer.core.project;

import de.adesso.budgeteer.core.common.Causes;
import lombok.Getter;

@Getter
public class ProjectException extends Exception {
    private final Causes<ProjectErrors> causes;

    public ProjectException(Causes<ProjectErrors> causes) {
        this.causes = causes;
    }

    public enum ProjectErrors {
        NAME_ALREADY_IN_USE,
        PROJECT_NOT_FOUND
    }
}