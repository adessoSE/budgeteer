package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StatisticsService {

    private Random random = new Random();

    /**
     * Returns the budget burned in each of the last numberOfWeeks weeks. All of the user's budgets are aggregated.
     *
     * @param userId        the id of the user whose budgets to consider
     * @param numberOfWeeks the number of weeks to look back into the past
     * @return list of values each being the monetary value of the budget burned in one week. The last entry belongs to the current week. A week is considered to start on Monday and end on Sunday.
     */
    public List<Money> getBudgetBurnedInPreviousWeeks(long userId, int numberOfWeeks) {
        List<Money> list = new ArrayList<Money>();
        for (int i = 0; i < numberOfWeeks; i++) {
            list.add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        }
        return list;
    }

    /**
     * Returns the average daily rate calculated for each of the last numberOfDays days. The average is calculated over all of the user's budgets.
     *
     * @param userId       the id of the user whose budgets to consider
     * @param numberOfDays the number of days to look back into the past
     * @return list of values each being the monetary value of the average daily rate that was earned for all people working on the user's budgets.
     */
    public List<Money> getAvgDailyRateForPreviousDays(long userId, int numberOfDays) {
        List<Money> list = new ArrayList<Money>();
        for (int i = 0; i < numberOfDays; i++) {
            list.add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        }
        return list;
    }

    /**
     * Returns the share of all budgets the given person has booked on.
     *
     * @param personId id of the person whose budget share to calculate
     * @return list of Share objects, totaling 100%
     */
    public List<Share> getBudgetDistribution(long personId) {
        List<Share> shares = new ArrayList<Share>();
        for (int i = 0; i < 5; i++) {
            shares.add(new Share(MoneyUtil.createMoneyFromCents(random.nextInt(100000)), "Budget " + i));
        }
        return shares;
    }

    /**
     * Returns the share of all people that have worked on the given budget.
     *
     * @param budgetId id of the budget whose person share to calculate
     * @return list of Share objects, totaling 100%
     */
    public List<Share> getPeopleDistribution(long budgetId) {
        List<Share> shares = new ArrayList<Share>();
        for (int i = 0; i < 5; i++) {
            shares.add(new Share(MoneyUtil.createMoneyFromCents(random.nextInt(100000)), "Person " + i));
        }
        return shares;
    }

    /**
     * Returns the actual and target budget values for the given person from the last numberOfWeeks weeks.
     *
     * @param personId      ID of the person whose data to load.
     * @param numberOfWeeks the number of weeks to go back into the past.
     * @return the week statistics for the last numberOfWeeks weeks
     */
    public TargetAndActual getWeekStatsForPerson(long personId, int numberOfWeeks) {
        TargetAndActual targetAndActual = new TargetAndActual();

        for (int i = 0; i < 5; i++) {
            BudgeteerSeries series = new BudgeteerSeries();
            series.setName("Budget " + i);
            for (int j = 0; j < numberOfWeeks; j++) {
                series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            }
            targetAndActual.getActualSeries().add(series);
        }

        BudgeteerSeries series = new BudgeteerSeries();
        series.setName("Target");
        for (int j = 0; j < numberOfWeeks; j++) {
            series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        }
        targetAndActual.setTargetSeries(series);

        return targetAndActual;
    }

    /**
     * Returns the actual and target budget values for the given person from the last numberOfMonths months.
     *
     * @param personId       ID of the person whose data to load.
     * @param numberOfMonths the number of months to go back into the past.
     * @return the month statistics for the last numberOfMonth months
     */
    public TargetAndActual getMonthStatsForPerson(long personId, int numberOfMonths) {
        TargetAndActual targetAndActual = new TargetAndActual();

        for (int i = 0; i < 5; i++) {
            BudgeteerSeries series = new BudgeteerSeries();
            series.setName("Budget " + i);
            for (int j = 0; j < numberOfMonths; j++) {
                series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            }
            targetAndActual.getActualSeries().add(series);
        }

        BudgeteerSeries series = new BudgeteerSeries();
        series.setName("Target");
        for (int j = 0; j < numberOfMonths; j++) {
            series.getValues().add(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        }
        targetAndActual.setTargetSeries(series);

        return targetAndActual;
    }

    /**
     * Returns the actual and target budget values for a set of given budgets aggregated by week.
     *
     * @param budgetFilter  The filter that identified the budgets whose data to load.
     * @param numberOfWeeks the number of weeks to go back into the past.
     * @return the week statistics for the last numberOfWeeks weeks
     */
    public TargetAndActual getWeekStatsForBudgets(BudgetTagFilter budgetFilter, int numberOfWeeks) {
        return getWeekStatsForPerson(1l, numberOfWeeks);
    }

    /**
     * Returns the actual and target budget values for a set of given budgets from the last numberOfMonths months.
     *
     * @param budgetFilter   The filter that identified the budgets whose data to load.
     * @param numberOfMonths the number of months to go back into the past.
     * @return the month statistics for the last numberOfMonths months
     */
    public TargetAndActual getMonthStatsForBudgets(BudgetTagFilter budgetFilter, int numberOfMonths) {
        return getMonthStatsForPerson(1l, numberOfMonths);
    }

    /**
     * Returns the actual and target budget values for a single budget aggregated by week.
     *
     * @param budgetId      ID of the budget whose data to load
     * @param numberOfWeeks the number of weeks to go back into the past.
     * @return the week statistics for the last numberOfWeeks weeks
     */
    public TargetAndActual getWeekStatsForBudget(long budgetId, int numberOfWeeks) {
        return getWeekStatsForPerson(1l, numberOfWeeks);
    }

    /**
     * Returns the actual and target budget values for a single budget from the last numberOfMonths months.
     *
     * @param budgetId       ID of the budget whose data to load
     * @param numberOfMonths the number of months to go back into the past.
     * @return the month statistics for the last numberOfMonths months
     */
    public TargetAndActual getMonthStatsForBudget(long budgetId, int numberOfMonths) {
        return getMonthStatsForPerson(1l, numberOfMonths);
    }
}
