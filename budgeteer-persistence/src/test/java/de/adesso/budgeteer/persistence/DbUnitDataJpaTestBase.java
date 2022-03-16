package de.adesso.budgeteer.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@DataJpaTest
@TestExecutionListeners({
  DbUnitTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class
})
public abstract class DbUnitDataJpaTestBase {}
