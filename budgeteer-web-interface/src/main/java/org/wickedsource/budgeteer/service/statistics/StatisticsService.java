package org.wickedsource.budgeteer.service.statistics;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StatisticsService {

    Random random = new Random();

    /**
     * Returns the budget burned in each of the last numberOfWeeks weeks. All of the user's budgets are aggregated.
     *
     * @param userId        the id of the user whose budgets to consider
     * @param numberOfWeeks the number of weeks to look back into the past
     * @return list of values each being the monetary value of the budget burned in one week. The last entry belongs to the current week. A week is considered to start on Monday and end on Sunday.
     */
    public List<Double> getBudgetBurnedInPreviousWeeks(long userId, int numberOfWeeks) {
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < numberOfWeeks; i++) {
            list.add(random.nextDouble());
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
    public List<Double> getAvgDailyRateForPreviousDays(long userId, int numberOfDays) {
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < numberOfDays; i++) {
            list.add(random.nextDouble());
        }
        return list;
    }

    /**
     * Returns the share of all budgets the given person has booked on.
     *
     * @param personId id of the person whose budget share to calculate
     * @return list of BudgetValue objects, totaling 100%
     */
    public List<BudgetValue> getBudgetDistribution(long personId) {
        List<BudgetValue> shares = new ArrayList<BudgetValue>();
        for (int i = 0; i < 5; i++) {
            shares.add(new BudgetValue(random.nextDouble(), "Budget " + i));
        }
        return shares;
    }

    /**
     * Returns the actual and target budget valued for the given person from the last numberOfWeeks weeks.
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
                series.getValues().add(random.nextDouble());
            }
            targetAndActual.getActualSeries().add(series);
        }

        BudgeteerSeries series = new BudgeteerSeries();
        series.setName("Target");
        for (int j = 0; j < numberOfWeeks; j++) {
            series.getValues().add(random.nextDouble());
        }
        targetAndActual.setTargetSeries(series);

        return targetAndActual;
    }

}
