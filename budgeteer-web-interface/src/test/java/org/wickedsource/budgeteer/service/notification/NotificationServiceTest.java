package org.wickedsource.budgeteer.service.notification;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

class NotificationServiceTest extends ServiceTestTemplate {

    private Date fixedDate = new Date();

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private NotificationService service;

    @Test
    void testGetNotifications() throws Exception {
        when(workRecordRepository.findByProjectId(1L)).thenReturn(Collections.singletonList(createWorkRecords()));
        when(workRecordRepository.countByProjectId(anyLong())).thenReturn(0L, 0L);
        when(budgetRepository.getMissingBudgetTotalsForProject(1L)).thenReturn(Collections.singletonList(createMissingBudgetTotal()));
        List<Notification> notifications = service.getNotifications(1L, 1L);
        Assertions.assertEquals(4, notifications.size());
    }

    @Test
    void testGetNotificationsForPerson() throws Exception {
        when(workRecordRepository.findByPersonId(1L)).thenReturn(Collections.singletonList(createWorkRecords()));
        List<Notification> notifications = service.getNotificationsForPerson(1L);
        Assertions.assertEquals(1, notifications.size());
        MissingDailyRateForBudgetNotification notification = (MissingDailyRateForBudgetNotification) notifications.get(0);
        Assertions.assertEquals(1L, notification.getPersonId());
        Assertions.assertEquals("person1", notification.getPersonName());
        Assertions.assertEquals(fixedDate, notification.getStartDate());
        Assertions.assertEquals(fixedDate, notification.getEndDate());
        Assertions.assertEquals("Budget1", notification.getBudgetName());
    }

    @Test
    void testGetNotificationsForBudget() throws Exception {
        when(budgetRepository.getMissingBudgetTotalForBudget(1L)).thenReturn(createMissingBudgetTotal());
        List<Notification> notifications = service.getNotificationsForBudget(1L);
        Assertions.assertEquals(1, notifications.size());
        MissingBudgetTotalNotification notification = (MissingBudgetTotalNotification) notifications.get(0);
        Assertions.assertEquals(1L, notification.getBudgetId());
        Assertions.assertEquals("budget1", notification.getBudgetName());
    }

    private WorkRecordEntity createWorkRecords() {
        WorkRecordEntity entity = new WorkRecordEntity();
        entity.setEditedManually(false);
        entity.setDate(fixedDate);
        entity.setDailyRate(Money.zero(CurrencyUnit.EUR));
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setName("Budget1");
        entity.setBudget(budgetEntity);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("person1");
        personEntity.setId(1L);
        entity.setPerson(personEntity);
        return entity;
    }

    private MissingBudgetTotalBean createMissingBudgetTotal() {
        return new MissingBudgetTotalBean(1L, "budget1");
    }
}
