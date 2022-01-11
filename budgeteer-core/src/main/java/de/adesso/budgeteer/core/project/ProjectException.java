package de.adesso.budgeteer.core.project;

import de.adesso.budgeteer.core.DomainException;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class ProjectException extends Exception
    implements DomainException<ProjectException.ProjectErrors> {
  private final Set<ProjectErrors> causes = new HashSet<>();

  public enum ProjectErrors {
    NAME_ALREADY_IN_USE,
    PROJECT_NOT_FOUND
  }
}
