package org.wickedsource.budgeteer.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRateBean;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private MissingDailyRateNotificationMapper missingDailyRateMapper;

    @Autowired
    private MissingBudgetTotalNotificationMapper missingBudgetTotalNotificationMapper;

    /**
     * Returns all notifications currently available for the given project
     *
     * @param projectId ID of the project whose notifications to load
     * @return list of notifications
     */
    public List<Notification> getNotifications(long projectId) {
        List<Notification> notifications = new ArrayList<Notification>();
        if (workRecordRepository.count() == 0) {
            notifications.add(new EmptyDatabaseNotification());
        }
        notifications.addAll(missingDailyRateMapper.map(workRecordRepository.getMissingDailyRatesForProject(projectId)));
        notifications.addAll(missingBudgetTotalNotificationMapper.map(budgetRepository.getMissingBudgetTotalsForProject(projectId)));
        return notifications;
    }

    /**
     * Returns all notifications currently available concerning the given person.
     *
     * @param personId id of the person about whom notifications should be returned.
     * @return list of notifications concerning the given person.
     */
    public List<Notification> getNotificationsForPerson(long personId) {
        MissingDailyRateBean missingDailyRatesForPerson = workRecordRepository.getMissingDailyRatesForPerson(personId);
        if (missingDailyRatesForPerson != null) {
            return Arrays.asList(missingDailyRateMapper.map(missingDailyRatesForPerson));
        } else {
            return new ArrayList<Notification>();
        }
    }

    /**
     * Returns all notifications currently available concerning the given budget.
     *
     * @param budgetId id of the budget about which notifications should be returned.
     * @return list of notifications concerning the given budget.
     */
    public List<Notification> getNotificationsForBudget(long budgetId) {
        MissingBudgetTotalBean missingBudgetTotalForBudget = budgetRepository.getMissingBudgetTotalForBudget(budgetId);
        if (missingBudgetTotalForBudget != null) {
            return Arrays.asList(missingBudgetTotalNotificationMapper.map(missingBudgetTotalForBudget));
        } else {
            return new ArrayList<Notification>();
        }
    }

}
