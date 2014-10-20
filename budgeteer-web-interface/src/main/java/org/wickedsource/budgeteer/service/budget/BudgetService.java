package org.wickedsource.budgeteer.service.budget;

import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.MoneyUtil;

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
        return new BudgetBaseData(1, "Budget " + budgetId);
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
     * Loads the detail data of a single budget.
     *
     * @param budgetId ID ID of the budget to load.
     * @return detail data for the requested budget.
     */
    public BudgetDetailData loadBudgetDetailData(long budgetId) {
        BudgetDetailData data = new BudgetDetailData();
        data.setLastUpdated(new Date());
        data.setName("Budget Title");
        data.setSpent(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        data.setTotal(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        data.setTags(Arrays.asList("Active"));
        data.setAvgDailyRate(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
        return data;
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
            data.setId(i);
            data.setLastUpdated(new Date());
            data.setName("Budget " + i);
            data.setSpent(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            data.setTotal(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            data.setTags(Arrays.asList("Active"));
            list.add(data);
        }
        return list;
    }

    /**
     * Loads the data of a budget to edit in the UI.
     *
     * @param budgetId ID of the budget whose data to load.
     * @return data object containing the data that can be changed in the UI.
     */
    public EditBudgetData loadBudgetToEdit(long budgetId) {
        EditBudgetData data = new EditBudgetData();
        data.setId(budgetId);
        data.setImportKey("123");
        data.setTags(Arrays.asList("Tag1", "Tag2", "Tag3"));
        data.setTitle("Budget 123");
        data.setTotal(MoneyUtil.createMoney(10000d));
        return data;
    }

    /**
     * Stores the data to the given budget.
     *
     * @param data the data to store in the database.
     */
    public void editBudget(EditBudgetData data) {

    }

}
