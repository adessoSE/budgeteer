package org.wickedsource.budgeteer;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.wickedsource.budgeteer.service.security.BudgeteerAuthenticationToken;

@SpringBootTest
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@Transactional
@TestExecutionListeners({
  DbUnitTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class
})
public abstract class IntegrationTestTemplate {

  @BeforeEach
  public void setAuthentication() {
    // set placeholder authentication
    SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));
  }
}
