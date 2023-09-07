package org.wickedsource.budgeteer.service.notification;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.budget.MissingBudgetTotalBean;
import de.adesso.budgeteer.persistence.record.MissingDailyRateBean;
import de.adesso.budgeteer.persistence.record.MissingDailyRateForBudgetBean;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

class NotificationServiceTest extends ServiceTestTemplate {

  private Date fixedDate = new Date();

  @MockBean private WorkRecordRepository workRecordRepository;
  @MockBean private BudgetRepository budgetRepository;
  @Autowired private NotificationService service;

  @Test
  void testGetNotifications() throws Exception {
    when(workRecordRepository.getMissingDailyRatesForProject(1L))
        .thenReturn(Arrays.asList(createMissingDailyRate()));
    when(workRecordRepository.countByProjectId(anyLong())).thenReturn(0L, 0L);
    when(budgetRepository.getMissingBudgetTotalsForProject(1L))
        .thenReturn(Arrays.asList(createMissingBudgetTotal()));
    List<Notification> notifications = service.getNotifications(1L, 1L);
    Assertions.assertEquals(3, notifications.size());
  }

  @Test
  void testGetNotificationsForPerson() throws Exception {
    when(workRecordRepository.getMissingDailyRatesForPerson(1L))
        .thenReturn(Arrays.asList(createMissingDailyRateForBudget()));
    List<Notification> notifications = service.getNotificationsForPerson(1L);
    Assertions.assertEquals(1, notifications.size());
    MissingDailyRateForBudgetNotification notification =
        (MissingDailyRateForBudgetNotification) notifications.get(0);
    Assertions.assertEquals(1L, notification.getPersonId());
    Assertions.assertEquals("person1", notification.getPersonName());
    Assertions.assertEquals(fixedDate, notification.getStartDate());
    Assertions.assertEquals(fixedDate, notification.getEndDate());
    Assertions.assertEquals("Budget1", notification.getBudgetName());
  }

  @Test
  void testGetNotificationsForBudget() throws Exception {
    when(budgetRepository.getMissingBudgetTotalForBudget(1L))
        .thenReturn(createMissingBudgetTotal());
    List<Notification> notifications = service.getNotificationsForBudget(1L);
    Assertions.assertEquals(1, notifications.size());
    MissingBudgetTotalNotification notification =
        (MissingBudgetTotalNotification) notifications.get(0);
    Assertions.assertEquals(1L, notification.getBudgetId());
    Assertions.assertEquals("budget1", notification.getBudgetName());
  }

  private MissingDailyRateBean createMissingDailyRate() {
    return new MissingDailyRateBean(1L, "person1", fixedDate, fixedDate);
  }

  private MissingDailyRateForBudgetBean createMissingDailyRateForBudget() {
    return new MissingDailyRateForBudgetBean(1L, "person1", fixedDate, fixedDate, "Budget1", 1L);
  }

  private MissingBudgetTotalBean createMissingBudgetTotal() {
    return new MissingBudgetTotalBean(1L, "budget1");
  }
}
