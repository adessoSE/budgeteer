package org.wickedsource.budgeteer;

import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.wickedsource.budgeteer.service.security.BudgeteerMethodSecurityExpressionRoot;

@ComponentScan(
    basePackages = {
      "de.adesso.budgeteer.core",
      "de.adesso.budgeteer.persistence",
      "org.wickedsource.budgeteer"
    })
public class IntegrationTestConfiguration {

  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
  }

  @Bean
  @Primary
  public BudgeteerMethodSecurityExpressionRoot budgeteerMethodSecurityExpressionRootBean() {
    BudgeteerMethodSecurityExpressionRoot root =
        Mockito.mock(BudgeteerMethodSecurityExpressionRoot.class);

    Mockito.when(root.canReadInvoice(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadContract(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadBudget(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadPerson(Mockito.anyLong())).thenReturn(true);
    Mockito.when(root.canReadProject(Mockito.anyLong())).thenReturn(true);

    return root;
  }
}
