package de.adesso.budgeteer.rest.security.authorization.aspects;

import de.adesso.budgeteer.rest.contract.model.ContractIdModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HasAccessToContractAspect {

  private final UserHasAccessToContractWrapper userHasAccessToContractWrapper;

  @Before(
      value =
          "(@within(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToContract) || @annotation(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToContract)) && args(contractIdModel, ..)")
  public void hasAccessToContract(ContractIdModel contractIdModel) {
    userHasAccessToContractWrapper.userHasAccessToContract(contractIdModel.getValue());
  }

  @Component
  public static class UserHasAccessToContractWrapper {
    @PreAuthorize("userHasAccessToContract(#contractId)")
    public void userHasAccessToContract(long contractId) {
      /* Method used to trigger method security */
    }
  }
}
