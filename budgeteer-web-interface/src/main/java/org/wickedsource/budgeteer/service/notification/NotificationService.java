package org.wickedsource.budgeteer.service.notification;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.budget.LimitReachedBean;
import de.adesso.budgeteer.persistence.budget.MissingBudgetTotalBean;
import de.adesso.budgeteer.persistence.record.MissingDailyRateForBudgetBean;
import de.adesso.budgeteer.persistence.record.PlanRecordRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService {

  @Autowired private WorkRecordRepository workRecordRepository;

  @Autowired private PlanRecordRepository planRecordRepository;

  @Autowired private BudgetRepository budgetRepository;

  @Autowired private MissingDailyRateNotificationMapper missingDailyRateMapper;

  @Autowired private MissingBudgetTotalNotificationMapper missingBudgetTotalNotificationMapper;

  @Autowired private LimitReachedNotificationMapper limitReachedNotificationMapper;

  @Autowired
  private MissingDailyRateForBudgetNotificationMapper missingDailyRateForBudgetNotificationMapper;

  /**
   * Returns all notifications currently available for the given project
   *
   * @param projectId ID of the project whose notifications to load
   * @return list of notifications
   */
  public List<Notification> getNotifications(long projectId, long userId) {
    List<Notification> notifications = new ArrayList<Notification>();
    if (workRecordRepository.countByProjectId(projectId) == 0) {
      notifications.add(new EmptyWorkRecordsNotification());
    }
    if (planRecordRepository.countByProjectId(projectId) == 0) {
      notifications.add(new EmptyPlanRecordsNotification());
    }
    notifications.addAll(
        budgetRepository.getMissingContractForProject(projectId).stream()
            .map(MissingContractForBudgetNotification::new)
            .collect(Collectors.toList()));
    notifications.addAll(
        missingDailyRateMapper.map(workRecordRepository.getMissingDailyRatesForProject(projectId)));
    notifications.addAll(
        missingBudgetTotalNotificationMapper.map(
            budgetRepository.getMissingBudgetTotalsForProject(projectId)));

    List<LimitReachedBean> beansList = budgetRepository.getBudgetsForProject(projectId);
    for (LimitReachedBean bean : beansList) {
      Double spentDouble = workRecordRepository.getSpentBudget(bean.getBudgetId());
      if (spentDouble != null) {
        Money budgetSpent = MoneyUtil.createMoneyFromCents(Math.round(spentDouble));
        LimitReachedBean limitReached =
            budgetRepository.getLimitReachedForBudget(bean.getBudgetId(), budgetSpent);
        if (limitReached != null)
          notifications.add(limitReachedNotificationMapper.map(limitReached));
      }
    }
    return notifications;
  }

  /**
   * Returns all notifications currently available concerning the given person.
   *
   * @param personId id of the person about whom notifications should be returned.
   * @return list of notifications concerning the given person.
   */
  public List<Notification> getNotificationsForPerson(long personId) {
    List<MissingDailyRateForBudgetBean> missingDailyRatesForPerson =
        workRecordRepository.getMissingDailyRatesForPerson(personId);
    return missingDailyRateForBudgetNotificationMapper.map(missingDailyRatesForPerson);
  }

  /**
   * Returns all notifications currently available concerning the given budget.
   *
   * @param budgetId id of the budget about which notifications should be returned.
   * @return list of notifications concerning the given budget.
   */
  public List<Notification> getNotificationsForBudget(long budgetId) {
    List<Notification> result = new LinkedList<Notification>();

    MissingBudgetTotalBean missingBudgetTotalForBudget =
        budgetRepository.getMissingBudgetTotalForBudget(budgetId);
    if (missingBudgetTotalForBudget != null) {
      result.add(missingBudgetTotalNotificationMapper.map(missingBudgetTotalForBudget));
    }

    var missingContract =
        budgetRepository
            .findById(budgetId)
            .filter(budget -> budget.getContract() == null)
            .map(BudgetEntity::getId)
            .map(MissingContractForBudgetNotification::new);
    missingContract.ifPresent(result::add);

    Double spentDouble = workRecordRepository.getSpentBudget(budgetId);
    if (spentDouble != null) {
      Money budgetSpent = MoneyUtil.createMoneyFromCents(Math.round(spentDouble));
      LimitReachedBean limitReached =
          budgetRepository.getLimitReachedForBudget(budgetId, budgetSpent);
      if (limitReached != null) result.add(limitReachedNotificationMapper.map(limitReached));
    }

    return result;
  }
}
