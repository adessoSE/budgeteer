package org.wickedsource.budgeteer.service.budget;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BudgetService {

    private Random random = new Random();

    /**
     * Loads all Budgets that the given user is qualified for and returns base data about them.
     *
     * @param userId ID of the logged in user
     * @return list of all budgets the user is qualified for
     */
    public List<BudgetBaseData> loadBudgetBaseDataForUser(long userId) {
        List<BudgetBaseData> list = new ArrayList<BudgetBaseData>();
        list.add(new BudgetBaseData(1, "Budget 1"));
        list.add(new BudgetBaseData(2, "Budget 2"));
        list.add(new BudgetBaseData(3, "Budget 3"));
        list.add(new BudgetBaseData(4, "Budget 4"));
        return list;
    }

    /**
     * Loads the base data of a single budget from the database.
     *
     * @param budgetId ID of the budget to load.
     * @return base data of the specified budget.
     */
    public BudgetBaseData loadBudgetBaseData(long budgetId) {
        return new BudgetBaseData(1, "Budget 1");
    }

    /**
     * Loads all tags applied to any budget of the given user.
     *
     * @param userId ID of the logged in user
     * @return all tags applied to any budget of the given user.
     */
    public List<String> loadBudgetTags(long userId) {
        return Arrays.asList("Active", "Completed", "Project 1", "Project 2");
    }

    /**
     * Loads all budgets the given user has access to that match the given filter.
     *
     * @param userId ID of the logged in user
     * @param filter the filter to apply when loading the budgets
     * @return list of budgets that match the filter.
     */
    public List<BudgetDetailData> loadBudgetsDetailData(long userId, BudgetTagFilter filter) {
        int count = 10;

        if (filter.getCombinationMode() == BudgetTagFilter.TagCombinationMode.AND) {
            for (int i = 0; i < filter.getSelectedTags().size(); i++) {
                count -= 1;
            }
        } else if (filter.getCombinationMode() == BudgetTagFilter.TagCombinationMode.OR) {
            for (int i = 0; i < filter.getSelectedTags().size(); i++) {
                count += 1;
            }
        }

        List<BudgetDetailData> list = new ArrayList<BudgetDetailData>();
        for (int i = 0; i < count; i++) {
            BudgetDetailData data = new BudgetDetailData();
            data.setLastUpdated(new Date());
            data.setName("Budget " + i);
            data.setSpent(random.nextDouble());
            data.setTotal(random.nextDouble());
            data.setTags(Arrays.asList("Active"));
            list.add(data);
        }
        return list;
    }

}
