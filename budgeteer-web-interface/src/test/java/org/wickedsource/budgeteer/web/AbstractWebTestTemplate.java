package org.wickedsource.budgeteer.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.common.old.MoneyUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.wicket.util.tester.WicketTester;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.imports.Import;
import org.wickedsource.budgeteer.service.imports.ImportService;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.pages.administration.Project;

@SpringBootTest
public abstract class AbstractWebTestTemplate {

  @Autowired BudgeteerApplication application;

  @MockBean protected ProjectService projectServiceMock;
  @MockBean protected BudgetService budgetServiceMock;
  @MockBean protected RecordService recordServiceMock;
  @MockBean protected UserService userServiceMock;
  @MockBean protected ImportService importServiceMock;
  @MockBean protected PersonService personServiceMock;

  private static WicketTester tester;

  @BeforeEach
  public void setUp() {
    // Provide default recordServiceMock mocks for tests.
    // If required, explicit behavior can be implemented for each test class in setupTest()
    when(projectServiceMock.findProjectById(anyLong()))
        .thenReturn(new Project(0, new Date(), new Date(), "test"));
    when(budgetServiceMock.loadBudgetBaseData(anyLong())).thenReturn(new BudgetBaseData(0, "test"));
    when(budgetServiceMock.loadBudgetDetailData(1L)).thenReturn(createBudget());
    when(recordServiceMock.getWeeklyAggregationForPerson(1L))
        .thenReturn(getWeeklyAggregationForPerson(1L));
    when(userServiceMock.getUsersInProject(1L)).thenReturn(getUsersInProject());
    when(importServiceMock.loadImports(1L)).thenReturn(createImports());
    when(personServiceMock.loadPersonDetailData(1L)).thenReturn(createPerson());

    tester = new WicketTester(application);
    loginAndSetProject();
    setupTest();
  }

  /** Subclasses can use this method to provide the configuration needed by each test. */
  protected abstract void setupTest();

  private void loginAndSetProject() {
    User user = new User();
    user.setId(1L);
    user.setName("username");
    BudgeteerSession.get().login(user);
    BudgeteerSession.get().setProjectSelected(true);
  }

  private List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
    Random random = new Random();
    List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
    for (int i = 0; i < 20; i++) {
      AggregatedRecord record = new AggregatedRecord();
      record.setAggregationPeriodTitle("Week #" + i);
      record.setAggregationPeriodStart(new Date());
      record.setAggregationPeriodStart(new Date());
      record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(random.nextInt(8000)));
      record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(random.nextInt(4000)));
      record.setHours(random.nextDouble());
      list.add(record);
    }
    return list;
  }

  private List<User> getUsersInProject() {
    List<User> users = new ArrayList<User>();
    for (int i = 1; i <= 5; i++) {
      User user = new User();
      user.setId(i);
      user.setName("User " + i);
      users.add(user);
    }
    return users;
  }

  private BudgetDetailData createBudget() {
    BudgetDetailData budgetDetailData = new BudgetDetailData();
    budgetDetailData.setId(1L);
    budgetDetailData.setName("Budget 1");
    budgetDetailData.setContractId(2L);
    budgetDetailData.setContractName("Contract");
    budgetDetailData.setTotal(Money.of(CurrencyUnit.EUR, 123));
    budgetDetailData.setSpent(Money.of(CurrencyUnit.EUR, 43));
    return budgetDetailData;
  }

  private List<Import> createImports() {
    List<Import> list = new ArrayList<Import>();
    for (int i = 0; i < 20; i++) {
      Import importRecord = new Import();
      importRecord.setEndDate(new Date());
      importRecord.setStartDate(new Date());
      importRecord.setImportDate(new Date());
      importRecord.setImportType("aproda import");
      list.add(importRecord);
    }
    return list;
  }

  private PersonDetailData createPerson() {
    PersonDetailData data = new PersonDetailData();
    data.setAverageDailyRate(MoneyUtil.createMoney(100.0));
    data.setName("Tom Hombergs");
    data.setBudgetBurned(MoneyUtil.createMoney(100000.00));
    data.setFirstBookedDate(new Date());
    data.setHoursBooked(100.0);
    data.setLastBookedDate(new Date());
    return data;
  }

  protected WicketTester getTester() {
    return tester;
  }
}
