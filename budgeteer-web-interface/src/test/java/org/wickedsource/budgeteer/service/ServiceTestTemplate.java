package org.wickedsource.budgeteer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wickedsource.budgeteer.service.security.BudgeteerMethodSecurityExpressionRoot;
import org.wickedsource.budgeteer.service.security.BudgeteerAuthenticationToken;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = {"classpath:spring-service.xml", "classpath:spring-repository-mock.xml"})
public abstract class ServiceTestTemplate {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private BudgeteerMethodSecurityExpressionRoot root;

    @BeforeEach
    public void resetMocks() {
        for (String name : context.getBeanDefinitionNames()) {
            if (!"workRecordDatabaseImporter".equals(name) && !"planRecordDatabaseImporter".equals(name)) {
                // excluding prototype beans with constructor arguments
                Object bean = context.getBean(name);
                if (MockUtil.isMock(bean)) {
                    Mockito.reset(bean);
                }
            }
        }

        this.setAuthorizationAndMock();
    }

    private void setAuthorizationAndMock() {
        // set a placeholder authentication
        SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));

        Mockito.when(root.canReadInvoice(Mockito.anyLong())).thenReturn(true);
        Mockito.when(root.canReadContract(Mockito.anyLong())).thenReturn(true);
        Mockito.when(root.canReadBudget(Mockito.anyLong())).thenReturn(true);
        Mockito.when(root.canReadPerson(Mockito.anyLong())).thenReturn(true);
        Mockito.when(root.canReadProject(Mockito.anyLong())).thenReturn(true);
    }

}
