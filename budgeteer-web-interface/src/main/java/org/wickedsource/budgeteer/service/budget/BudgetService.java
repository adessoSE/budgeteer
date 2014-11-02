package org.wickedsource.budgeteer.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.service.UnknownEntityException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BudgetService {

    private Random random = new Random();

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetBaseDataMapper budgetBaseDataMapper;

    /**
     * Loads all Budgets that the given user is qualified for and returns base data about them.
     *
     * @param projectId ID of the project
     * @return list of all budgets the user is qualified for
     */
    public List<BudgetBaseData> loadBudgetBaseDataForProject(long projectId) {
        List<BudgetEntity> budgets = budgetRepository.findByProjectId(projectId);
        return budgetBaseDataMapper.map(budgets);
    }

    /**
     * Loads the base data of a single budget from the database.
     *
     * @param budgetId ID of the budget to load.
     * @return base data of the specified budget.
     */
    public BudgetBaseData loadBudgetBaseData(long budgetId) {
        BudgetEntity budget = budgetRepository.findOne(budgetId);
        return budgetBaseDataMapper.map(budget);
    }

    /**
     * Loads all tags assigned to any budget of the given user.
     *
     * @param projectId ID of the project
     * @return all tags assigned to any budget of the given user.
     */
    public List<String> loadBudgetTags(long projectId) {
        return new ArrayList<String>(budgetRepository.getAllTagsInProject(projectId));
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
     * @param projectId ID of the project
     * @param filter    the filter to apply when loading the budgets
     * @return list of budgets that match the filter.
     */
    public List<BudgetDetailData> loadBudgetsDetailData(long projectId, BudgetTagFilter filter) {
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
            data.setUnplanned(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
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
        BudgetEntity budget = budgetRepository.findOne(budgetId);
        if (budget == null) {
            throw new UnknownEntityException(BudgetEntity.class, budgetId);
        }
        EditBudgetData data = new EditBudgetData();
        data.setId(budget.getId());
        data.setTotal(budget.getTotal());
        data.setTitle(budget.getName());
        data.setTags(budget.getTags());
        data.setImportKey(budget.getImportKey());
        return data;
    }

    /**
     * Stores the data to the given budget.
     *
     * @param data the data to store in the database.
     */
    public void editBudget(EditBudgetData data) {
        assert data != null;
        BudgetEntity budget = budgetRepository.findOne(data.getId());
        if (budget == null) {
            throw new UnknownEntityException(BudgetEntity.class, data.getId());
        }
        budget.setImportKey(data.getImportKey());
        budget.setTags(data.getTags());
        budget.setTotal(data.getTotal());
        budget.setName(data.getTitle());
    }

    /**
     * Returns the units in which the user can have his budget values displayed. One unit is active, the others are
     * inactive.
     *
     * @param projectId ID of the project whose budgets to load
     * @return a list containing all available budget units, one of which is currently active.
     */
    public List<Double> loadBudgetUnits(long projectId) {
        List<Double> units = new ArrayList<Double>();
        units.add(1d);
        units.add(500d);
        units.add(790d);
        return units;
    }

}
