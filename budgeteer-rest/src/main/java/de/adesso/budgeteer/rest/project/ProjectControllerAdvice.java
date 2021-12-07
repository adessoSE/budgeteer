package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.rest.project.errors.CreateProjectError;
import de.adesso.budgeteer.rest.project.errors.UpdateProjectError;
import de.adesso.budgeteer.rest.project.exceptions.CreateProjectException;
import de.adesso.budgeteer.rest.project.exceptions.UpdateProjectException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProjectControllerAdvice {
    @ExceptionHandler(CreateProjectException.class)
    public ResponseEntity<CreateProjectError> handleCreateProjectException(CreateProjectException createProjectException) {
        var causes = createProjectException.getProjectException().getCauses().getAll();

        var createProjectError = new CreateProjectError();
        createProjectError.setNamealreadyInUse(causes.contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));

        return ResponseEntity.badRequest().body(createProjectError);
    }

    @ExceptionHandler(UpdateProjectException.class)
    public ResponseEntity<UpdateProjectError> handleUpdateProjectException(UpdateProjectException updateProjectException) {
        var causes = updateProjectException.getProjectException().getCauses().getAll();

        var updateProjectError = new UpdateProjectError();
        updateProjectError.setProjectNotFound(causes.contains(ProjectException.ProjectErrors.PROJECT_NOT_FOUND));
        updateProjectError.setNameAlreadyInUse(causes.contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));

        return ResponseEntity.badRequest().body(updateProjectError);
    }
}
