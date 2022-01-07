package de.adesso.budgeteer.rest.security.authorization.aspects;

import de.adesso.budgeteer.rest.invoice.model.InvoiceIdModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HasAccessToInvoiceAspect {

  private final UserHasAccessToInvoiceWrapper userHasAccessToInvoiceWrapper;

  @Before(
      value =
          "(@within(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToInvoice) || @annotation(de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToInvoice)) && args(invoiceIdModel, ..)")
  public void hasAccessToInvoice(InvoiceIdModel invoiceIdModel) {
    userHasAccessToInvoiceWrapper.userHasAccessToInvoice(invoiceIdModel.getValue());
  }

  @Component
  public static class UserHasAccessToInvoiceWrapper {
    @PreAuthorize("userHasAccessToInvoice(#invoiceId)")
    public void userHasAccessToInvoice(long invoiceId) {
      /* Method used to trigger method security */
    }
  }
}
