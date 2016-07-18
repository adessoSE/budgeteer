package org.wickedsource.budgeteer.service.budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.BudgetTagEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.UnknownEntityException;
import org.wickedsource.budgeteer.service.contract.ContractDataMapper;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups.OptionGroup;


@Service
@Transactional
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetBaseDataMapper budgetBaseDataMapper;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private DailyRateRepository rateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractDataMapper contractDataMapper;

    /**
     * Loads all Budgets that the given user is qualified for and returns base data about them.
     *
     * @param projectId ID of the project
     * @return list of all budgets the user is qualified for
     */
    public List<BudgetBaseData> loadBudgetBaseDataForProject(long projectId) {
        List<BudgetEntity> budgets = budgetRepository.findByProjectIdOrderByNameAsc(projectId);
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
     * Loads the base data of a single budget from the database.
     *
     * @param personId ID of the person for which the budget should be loaded.
     * @return base data of the specified budget.
     */
    public List<BudgetBaseData> loadBudgetBaseDataByPersonId(long personId) {
        List<BudgetEntity> budget = budgetRepository.findByPersonId(personId);
        return budgetBaseDataMapper.map(budget);
    }

    /**
     * Loads all tags assigned to any budget of the given user.
     *
     * @param projectId ID of the project
     * @return all tags assigned to any budget of the given user.
     */
    public List<String> loadBudgetTags(long projectId) {
        return budgetRepository.getAllTagsInProject(projectId);
    }

    /**
     * Loads the detail data of a single budget.
     *
     * @param budgetId ID ID of the budget to load.
     * @return detail data for the requested budget.
     */
    public BudgetDetailData loadBudgetDetailData(long budgetId) {
        BudgetEntity budget = budgetRepository.findOne(budgetId);
        return enrichBudgetEntity(budget);
    }

    private BudgetDetailData enrichBudgetEntity(BudgetEntity entity) {
        Date lastUpdated = workRecordRepository.getLatestWordRecordDate(entity.getId());
        Double spentBudgetInCents = workRecordRepository.getSpentBudget(entity.getId());
        Double plannedBudgetInCents = planRecordRepository.getPlannedBudget(entity.getId());
        Double avgDailyRateInCents = workRecordRepository.getAverageDailyRate(entity.getId());

        BudgetDetailData data = new BudgetDetailData();
        data.setId(entity.getId());
        data.setLastUpdated(lastUpdated);
        data.setName(entity.getName());
        data.setSpent(toMoneyNullsafe(spentBudgetInCents));
        data.setTotal(entity.getTotal());
        data.setTags(mapEntitiesToTags(entity.getTags()));
        data.setAvgDailyRate(toMoneyNullsafe(avgDailyRateInCents));
        data.setUnplanned(entity.getTotal().minus(toMoneyNullsafe(plannedBudgetInCents)));
        data.setContractName(entity.getContract() == null ? null : entity.getContract().getName() );
        data.setContractId(entity.getContract() == null ? 0 : entity.getContract().getId() );
        return data;
    }

    private Money toMoneyNullsafe(Double cents) {
        if (cents == null) {
            return MoneyUtil.createMoneyFromCents(0l);
        } else {
            return MoneyUtil.createMoneyFromCents(Math.round(cents));
        }
    }

    private List<String> mapEntitiesToTags(List<BudgetTagEntity> tagEntities) {
        List<String> tags = new ArrayList<String>();
        for (BudgetTagEntity entity : tagEntities) {
            tags.add(entity.getTag());
        }
        return tags;
    }

    private List<BudgetTagEntity> mapTagsToEntities(List<String> tags, BudgetEntity budget) {
        List<BudgetTagEntity> entities = new ArrayList<BudgetTagEntity>();
        if (tags != null) {
            for (String tag : tags) {
                BudgetTagEntity entity = new BudgetTagEntity();
                entity.setTag(tag);
                entity.setBudget(budget);
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * Loads all budgets the given user has access to that match the given filter.
     *
     * @param projectId ID of the project
     * @param filter    the filter to apply when loading the budgets
     * @return list of budgets that match the filter.
     */
    public List<BudgetDetailData> loadBudgetsDetailData(long projectId, BudgetTagFilter filter) {
        List<BudgetEntity> budgets;
        if (filter.getSelectedTags().isEmpty()) {
            budgets = budgetRepository.findByProjectIdOrderByNameAsc(projectId);
        } else {
            budgets = budgetRepository.findByAtLeastOneTag(projectId, filter.getSelectedTags());
        }
        List<BudgetDetailData> dataList = new ArrayList<BudgetDetailData>();
        for (BudgetEntity entity : budgets) {
            // TODO: 4 additional database queries per loop! These can yet be optimized to 4 queries total!
            dataList.add(enrichBudgetEntity(entity));
        }
        return dataList;
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
        data.setTags(mapEntitiesToTags(budget.getTags()));
        data.setImportKey(budget.getImportKey());
        data.setContract(contractDataMapper.map(budget.getContract()));
        return data;
    }

    /**
     * Stores the data to the given budget.
     *
     * @param data the data to store in the database.
     */
    public void saveBudget(EditBudgetData data) {
        assert data != null;
        BudgetEntity budget = new BudgetEntity();
        if (data.getId() != 0) {
            budget = budgetRepository.findOne(data.getId());
        } else {
            ProjectEntity project = projectRepository.findOne(data.getProjectId());
            budget.setProject(project);
        }
        budget.setImportKey(data.getImportKey());
        budget.setTotal(data.getTotal());
        budget.setName(data.getTitle());
        budget.getTags().clear();
        budget.getTags().addAll(mapTagsToEntities(data.getTags(), budget));
        if(data.getContract() == null) {
            budget.setContract(null);
        } else {
            ContractEntity contractEntity = contractRepository.findOne(data.getContract().getContractId());
            budget.setContract(contractEntity);
        }
        budgetRepository.save(budget);
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
        List<Money> rates = rateRepository.getDistinctRatesInCents(projectId);
        for (Money rate : rates) {
            units.add(rate.getAmount().doubleValue());
        }
        return units;
    }

    public void deleteBudget(long id) {
        budgetRepository.delete(id);
    }

    public List<BudgetDetailData> loadBudgetByContract(long cId){
        List<BudgetDetailData> result = new LinkedList<BudgetDetailData>();
        List<BudgetEntity> temp =budgetRepository.findByContractId(cId);
        if(temp != null){
            for(BudgetEntity b : temp){
                result.add(enrichBudgetEntity(b));
            }
        }
        return result;
    }

    public List<OptionGroup<BudgetBaseData>> getPossibleBudgetDataForPersonAndProject(long projectId, long personId){

        List<BudgetBaseData> allBudgets = loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
        List<BudgetBaseData> personalBudgets = loadBudgetBaseDataForPerson(personId);

        allBudgets.removeAll(personalBudgets);

        List<OptionGroup<BudgetBaseData>> result = new LinkedList();
        if(!personalBudgets.isEmpty()) {
            result.add(new OptionGroup<>("Used", personalBudgets));
        }
        if(! allBudgets.isEmpty()){
            result.add(new OptionGroup<>("All", allBudgets));
        }

        return result;
    }

    private List<BudgetBaseData> loadBudgetBaseDataForPerson(long personId) {
        List<BudgetEntity> budgets = budgetRepository.findByPersonId(personId);
        return budgetBaseDataMapper.map(budgets);
    }

}
