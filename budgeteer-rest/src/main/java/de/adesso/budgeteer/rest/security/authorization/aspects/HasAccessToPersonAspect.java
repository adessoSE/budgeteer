package de.adesso.budgeteer.rest.security.authorization.aspects;

import de.adesso.budgeteer.rest.person.model.PersonIdModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HasAccessToPersonAspect {

  private final UserHasAccessToPersonWrapper userHasAccessToPersonWrapper;

  @Before(
      value =
          "(@within(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToPerson) || @annotation(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToPerson)) && args(personIdModel, ..))")
  public void hasAccessToPerson(PersonIdModel personIdModel) {
    userHasAccessToPersonWrapper.userHasAccessToPersonWrapper(personIdModel.getValue());
  }

  @Component
  public static class UserHasAccessToPersonWrapper {
    @PreAuthorize("userHasAccessToPerson(#personId)")
    public void userHasAccessToPersonWrapper(long personId) {
      /* Method used to trigger method security */
    }
  }
}
