package org.wickedsource.budgeteer.service.notification;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRateBean;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRateForBudgetBean;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class NotificationServiceTest extends ServiceTestTemplate {

    private Date fixedDate = new Date();

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private NotificationService service;

    @Test
    public void testGetNotifications() throws Exception {
        when(workRecordRepository.getMissingDailyRatesForProject(1l)).thenReturn(Arrays.asList(createMissingDailyRate()));
        when(workRecordRepository.countByProjectId(anyLong())).thenReturn(0l, 0l);
        when(budgetRepository.getMissingBudgetTotalsForProject(1l)).thenReturn(Arrays.asList(createMissingBudgetTotal()));

        List<Notification> notifications = service.getNotifications(1l);
        Assert.assertEquals(4, notifications.size());
    }

    @Test
    public void testGetNotificationsForPerson() throws Exception {
        when(workRecordRepository.getMissingDailyRatesForPerson(1l)).thenReturn(Arrays.asList(createMissingDailyRateForBudget()));
        List<Notification> notifications = service.getNotificationsForPerson(1l);
        Assert.assertEquals(1, notifications.size());
        MissingDailyRateForBudgetNotification notification = (MissingDailyRateForBudgetNotification) notifications.get(0);
        Assert.assertEquals(1l, notification.getPersonId());
        Assert.assertEquals("person1", notification.getPersonName());
        Assert.assertEquals(fixedDate, notification.getStartDate());
        Assert.assertEquals(fixedDate, notification.getEndDate());
        Assert.assertEquals("Budget1", notification.getBudgetName());
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

    private MissingDailyRateForBudgetBean createMissingDailyRateForBudget() {
        return new MissingDailyRateForBudgetBean(1l, "person1", fixedDate, fixedDate, "Budget1");
    }

    private MissingBudgetTotalBean createMissingBudgetTotal() {
        return new MissingBudgetTotalBean(1l, "budget1");
    }
}
