package org.wickedsource.budgeteer.service.notification;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.LimitReachedBean;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.user.UserService;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MissingDailyRateNotificationMapper missingDailyRateMapper;

    @Autowired
    private MissingBudgetTotalNotificationMapper missingBudgetTotalNotificationMapper;

    @Autowired
    private LimitReachedNotificationMapper limitReachedNotificationMapper;

    @Autowired
    private MissingDailyRateForBudgetNotificationMapper missingDailyRateForBudgetNotificationMapper;

    /**
     * Returns all notifications currently available for the given project
     *
     * @param projectId ID of the project whose notifications to load
     * @return list of notifications
     */
    public List<Notification> getNotifications(long projectId, long userId) {
        List<Notification> notifications = new ArrayList<>();
        if (workRecordRepository.countByProjectId(projectId) == 0) {
            notifications.add(new EmptyWorkRecordsNotification());
        }
        if (planRecordRepository.countByProjectId(projectId) == 0) {
            notifications.add(new EmptyPlanRecordsNotification());
        }
        notifications.addAll(budgetRepository.getMissingContractForProject(projectId));
        notifications.addAll(missingDailyRateMapper.map(getMissingDailyRatesForProject(projectId)));
        notifications.addAll(missingBudgetTotalNotificationMapper.map(budgetRepository.getMissingBudgetTotalsForProject(projectId)));

        List<LimitReachedBean> beansList = budgetRepository.getBudgetsForProject(projectId);
        for (LimitReachedBean bean : beansList) {
            Double spentDouble = workRecordRepository.getSpentBudget(bean.getBudgetId());
            if (spentDouble != null) {
                Money budgetSpent = MoneyUtil.createMoneyFromCents(Math.round(spentDouble));
                LimitReachedBean limitReached = budgetRepository.getLimitReachedForBudget(bean.getBudgetId(), budgetSpent);
                if (limitReached != null)
                    notifications.add(limitReachedNotificationMapper.map(limitReached));
            }
        }

        UserEntity user = userRepository.findOne(userId);
        if (user != null) {
            if (user.getMail() == null) {
                notifications.add(new MissingMailNotification(user.getId()));
            }

            if (!user.getMailVerified() && user.getMail() != null) {
                notifications.add(new MailNotVerifiedNotification(user.getId(), user.getMail()));
            }
        }

        return notifications;
    }

    private List<MissingDailyRateBean> getMissingDailyRatesForProject(long projectId) {
        List<MissingDailyRateBean> result = new ArrayList<>();
        List<WorkRecordEntity> dailyRatesForProject = workRecordRepository.findByProjectId(projectId);
        dailyRatesForProject.sort((o1, o2) -> {
            int names = o1.getPerson().getName().compareTo(o2.getPerson().getName());
            if (names == 0) {
                int dates = o1.getDate().compareTo(o2.getDate());
                if(dates == 0){
                    return o1.getBudget().getName().compareTo(o2.getBudget().getName());
                }else{
                    return dates;
                }
            } else {
                return names;
            }
        });

        Date endDate = null;
        Date startDate = null;

        for(int i = 0; i < dailyRatesForProject.size() - 1; i++){
            WorkRecordEntity rate1 = dailyRatesForProject.get(i);
            WorkRecordEntity rate2 = dailyRatesForProject.get(i+1);
            if(!rate1.getDailyRate().isZero()){
                continue;
            }
            if(startDate == null) {
                endDate = rate2.getDate();
                startDate = rate1.getDate();
            }

            if(rate1.getDailyRate().isZero() && rate2.getDailyRate().isZero()
                    && rate1.getPerson().getId() == rate2.getPerson().getId()
                    && rate1.getBudget().getId() == rate2.getBudget().getId()){
                endDate = rate2.getDate();
                if(i+1 == dailyRatesForProject.size() - 1){
                    result.add(new MissingDailyRateBean(rate1.getPerson().getId(),
                            rate1.getPerson().getName(), (Date) startDate.clone(), (Date) endDate.clone()));
                }
            }else{
                if(rate1.getPerson().getId() != rate2.getPerson().getId() || !rate2.getDailyRate().isZero()){
                    endDate = rate1.getDate();
                }
                MissingDailyRateBean missingDailyRateBean = new MissingDailyRateBean(rate1.getPerson().getId(),
                        rate1.getPerson().getName(), (Date) startDate.clone(), (Date) endDate.clone());
                result.add(missingDailyRateBean);
                startDate = null;
            }
        }
        return result;
    }

    private List<MissingDailyRateForBudgetBean> getMissingDailyRatesForPerson(long personId) {
        List<MissingDailyRateForBudgetBean> result = new ArrayList<>();
        List<WorkRecordEntity> dailyRatesForProject = workRecordRepository.findByPersonId(personId);
        dailyRatesForProject.sort((o1, o2) -> {
            int dates = o1.getDate().compareTo(o2.getDate());
            if(dates == 0){
                return o1.getBudget().getName().compareTo(o2.getBudget().getName());
            }else{
                return dates;
            }
        });

        Date endDate = null;
        Date startDate = null;

        for(int i = 0; i < dailyRatesForProject.size() - 1; i++){
            WorkRecordEntity rate1 = dailyRatesForProject.get(i);
            WorkRecordEntity rate2 = dailyRatesForProject.get(i+1);
            if(!rate1.getDailyRate().isZero()){
                continue;
            }
            if(startDate == null) {
                endDate = rate2.getDate();
                startDate = rate1.getDate();
            }

            if(rate1.getDailyRate().isZero() && rate2.getDailyRate().isZero()
                    && rate1.getBudget().getId() == rate2.getBudget().getId()){
                endDate = rate2.getDate();
                if(i+1 == dailyRatesForProject.size() - 1){
                    result.add(new MissingDailyRateForBudgetBean(rate1.getPerson().getId(),
                            rate1.getPerson().getName(), (Date) startDate.clone(), (Date) endDate.clone(), rate1.getBudget().getName()));
                }
            }else{
                MissingDailyRateForBudgetBean missingDailyRateBean = new MissingDailyRateForBudgetBean(rate1.getPerson().getId(),
                        rate1.getPerson().getName(), (Date) startDate.clone(), (Date) endDate.clone(), rate1.getBudget().getName());
                result.add(missingDailyRateBean);
                startDate = null;
            }
        }
        return result;
    }

    /**
     * Returns all notifications currently available concerning the given person.
     *
     * @param personId id of the person about whom notifications should be returned.
     * @return list of notifications concerning the given person.
     */
    public List<Notification> getNotificationsForPerson(long personId) {
        List<MissingDailyRateForBudgetBean> missingDailyRatesForPerson = getMissingDailyRatesForPerson(personId);
        return missingDailyRateForBudgetNotificationMapper.map(missingDailyRatesForPerson);
    }



    /**
     * Returns all notifications currently available concerning the given budget.
     *
     * @param budgetId id of the budget about which notifications should be returned.
     * @return list of notifications concerning the given budget.
     */
    public List<Notification> getNotificationsForBudget(long budgetId) {
        List<Notification> result = new LinkedList<>();

        MissingBudgetTotalBean missingBudgetTotalForBudget = budgetRepository.getMissingBudgetTotalForBudget(budgetId);
        if (missingBudgetTotalForBudget != null) {
            result.add(missingBudgetTotalNotificationMapper.map(missingBudgetTotalForBudget));
        }

        MissingContractForBudgetNotification missingContract = budgetRepository.getMissingContractForBudget(budgetId);
        if (missingContract != null) {
            result.add(missingContract);
        }

        Double spentDouble = workRecordRepository.getSpentBudget(budgetId);
        if (spentDouble != null) {
            Money budgetSpent = MoneyUtil.createMoneyFromCents(Math.round(spentDouble));
            LimitReachedBean limitReached = budgetRepository.getLimitReachedForBudget(budgetId, budgetSpent);
            if (limitReached != null)
                result.add(limitReachedNotificationMapper.map(limitReached));
        }

        return result;
    }

}
