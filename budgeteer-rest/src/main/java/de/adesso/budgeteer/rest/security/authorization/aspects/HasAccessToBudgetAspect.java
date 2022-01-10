package de.adesso.budgeteer.rest.security.authorization.aspects;

import de.adesso.budgeteer.rest.budget.model.BudgetIdModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HasAccessToBudgetAspect {

  private final UserHasAccessToBudgetWrapper userHasAccessToBudgetWrapper;

  @Before(
      value =
          "(@within(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToBudget) || @annotation(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToBudget)) && args(budgetIdModel, ..)")
  public void hasAccessToBudget(BudgetIdModel budgetIdModel) {
    userHasAccessToBudgetWrapper.userHasAccessToBudget(budgetIdModel.getValue());
  }

  @Component
  public static class UserHasAccessToBudgetWrapper {
    @PreAuthorize("userHasAccessToBudget(#budgetId)")
    public void userHasAccessToBudget(long budgetId) {
      /* Method used to trigger method security */
    }
  }
}
