package org.wickedsource.budgeteer;

import javax.transaction.Transactional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
@Transactional
@TestExecutionListeners({DbUnitTestExecutionListener.class, DirtiesContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
public abstract class IntegrationTestTemplate {}
