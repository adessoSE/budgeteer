package de.adesso.budgeteer;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import de.adesso.budgeteer.service.security.BudgeteerAuthenticationToken;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@Transactional
@TestExecutionListeners({DbUnitTestExecutionListener.class, DirtiesContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public abstract class IntegrationTestTemplate {

    @BeforeEach
    public void setAuthentication() {
        // set placeholder authentication
        SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));
    }

}
