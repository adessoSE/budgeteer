package de.adesso.budgeteer.rest.security.authorization.aspects;

import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@RequiredArgsConstructor
@Component
public class HasAccessToProjectAspect {

  private final UserHasAccessToProjectWrapper userHasAccessToProjectWrapper;

  @Before(
      value =
          "(@within(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToProject) || @annotation(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToProject)) && args(projectIdModel, ..)")
  public void hasAccessToProject(ProjectIdModel projectIdModel) {
    userHasAccessToProjectWrapper.userHasAccessToProject(projectIdModel.getValue());
  }

  @Component
  public static class UserHasAccessToProjectWrapper {
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public void userHasAccessToProject(long projectId) {
      /* Method used to trigger method security */
    }
  }
}
