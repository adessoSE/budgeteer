package org.wickedsource.budgeteer.service.budget;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    /**
     * Loads all Budgets that the given user is qualified for and returns base data about them.
     *
     * @param userId ID of the logged in user
     * @return list of all budgets the user is qualified for
     */
    public List<BudgetBaseData> loadBudgetBaseData(long userId) {
        List<BudgetBaseData> list = new ArrayList<BudgetBaseData>();
        list.add(new BudgetBaseData(1, "Budget 1"));
        list.add(new BudgetBaseData(2, "Budget 2"));
        list.add(new BudgetBaseData(3, "Budget 3"));
        list.add(new BudgetBaseData(4, "Budget 4"));
        return list;
    }


}
