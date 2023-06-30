package org.wickedsource.budgeteer.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wickedsource.budgeteer.service.security.BudgeteerAuthenticationToken;
import org.wickedsource.budgeteer.service.security.BudgeteerMethodSecurityExpressionRoot;

@SpringBootTest
public abstract class ServiceTestTemplate {

  @Autowired private ApplicationContext context;

  @MockBean private BudgeteerMethodSecurityExpressionRoot root;

  @BeforeEach
  public void resetMocks() {
    for (String name : context.getBeanDefinitionNames()) {
      if (!"workRecordDatabaseImporter".equals(name)
          && !"planRecordDatabaseImporter".equals(name)) {
        // excluding prototype beans with constructor arguments
        Object bean = context.getBean(name);
        if (Mockito.mockingDetails(bean).isMock()) {
          Mockito.reset(bean);
        }
      }
    }

    this.setAuthorizationAndMock();
  }

  private void setAuthorizationAndMock() {
    // set a placeholder authentication
    SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));
    Mockito.when(root.canReadContract(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadBudget(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadPerson(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadProject(Mockito.anyLong())).thenReturn(true);
  }
}
