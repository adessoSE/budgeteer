package org.wickedsource.budgeteer.service.notification;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRateBean;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class NotificationServiceTest extends ServiceTestTemplate {

    private Date fixedDate = new Date();

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private NotificationService service;

    @Test
    public void testGetNotifications() throws Exception {
        when(recordRepository.getMissingDailyRatesForProject(1l)).thenReturn(Arrays.asList(createMissingDailyRate()));
        when(budgetRepository.getMissingBudgetTotalsForProject(1l)).thenReturn(Arrays.asList(createMissingBudgetTotal()));
        List<Notification> notifications = service.getNotifications(1l);
        Assert.assertEquals(2, notifications.size());
    }

    @Test
    public void testGetNotificationsForPerson() throws Exception {
        when(recordRepository.getMissingDailyRatesForPerson(1l)).thenReturn(createMissingDailyRate());
        List<Notification> notifications = service.getNotificationsForPerson(1l);
        Assert.assertEquals(1, notifications.size());
        MissingDailyRateNotification notification = (MissingDailyRateNotification) notifications.get(0);
        Assert.assertEquals(1l, notification.getPersonId());
        Assert.assertEquals("person1", notification.getPersonName());
        Assert.assertEquals(fixedDate, notification.getStartDate());
        Assert.assertEquals(fixedDate, notification.getEndDate());
    }

    @Test
    public void testGetNotificationsForBudget() throws Exception {
        when(budgetRepository.getMissingBudgetTotalForBudget(1l)).thenReturn(createMissingBudgetTotal());
        List<Notification> notifications = service.getNotificationsForBudget(1l);
        Assert.assertEquals(1, notifications.size());
        MissingBudgetTotalNotification notification = (MissingBudgetTotalNotification) notifications.get(0);
        Assert.assertEquals(1l, notification.getBudgetId());
        Assert.assertEquals("budget1", notification.getBudgetName());
    }

    private MissingDailyRateBean createMissingDailyRate() {
        return new MissingDailyRateBean(1l, "person1", fixedDate, fixedDate);
    }

    private MissingBudgetTotalBean createMissingBudgetTotal() {
        return new MissingBudgetTotalBean(1l, "budget1");
    }
}
