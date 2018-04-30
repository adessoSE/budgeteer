package org.wickedsource.budgeteer.service.budget;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.BudgetTagEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.person.DailyRateRepository;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BudgetServiceTest extends ServiceTestTemplate {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private DailyRateRepository rateRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    public void testLoadBudgetBaseDataForProject() {
        when(budgetRepository.findByProjectIdOrderByNameAsc(1l)).thenReturn(Arrays.asList(createBudgetEntity()));
        List<BudgetBaseData> budgets = budgetService.loadBudgetBaseDataForProject(1l);
        Assert.assertEquals(1, budgets.size());
        Assert.assertEquals(1, budgets.get(0).getId());
        Assert.assertEquals("Budget 123", budgets.get(0).getName());
    }

    @Test
    public void testLoadBudgetBaseData() {
        when(budgetRepository.findOne(1l)).thenReturn(createBudgetEntity());
        BudgetBaseData data = budgetService.loadBudgetBaseData(1l);
        Assert.assertEquals(1l, data.getId());
        Assert.assertEquals("Budget 123", data.getName());
    }

    @Test
    public void testLoadBudgetTags() {
        List<String> tags = new ArrayList<String>();
        tags.add("1");
        tags.add("2");
        when(budgetRepository.getAllTagsInProject(1l)).thenReturn(tags);
        List<String> loadedTags = budgetService.loadBudgetTags(1l);
        Assert.assertEquals(2, loadedTags.size());
        Assert.assertTrue(loadedTags.contains("1"));
        Assert.assertTrue(loadedTags.contains("2"));
    }

    @Test
    public void testLoadBudgetDetailData() {
        Date date = new Date();
        when(budgetRepository.findOne(1l)).thenReturn(createBudgetEntity());
        when(workRecordRepository.getLatestWorkRecordDate(1l)).thenReturn(date);
        when(workRecordRepository.getSpentBudget(1l)).thenReturn(100000.0);
        when(planRecordRepository.getPlannedBudget(1l)).thenReturn(200000.0);
        when(workRecordRepository.getAverageDailyRate(1l)).thenReturn(50000.0);
        BudgetDetailData data = budgetService.loadBudgetDetailData(1l);
        Assert.assertEquals(100000.0d, data.getSpent().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(-100000.0d, data.getUnplanned().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(50000.0d, data.getAvgDailyRate().getAmountMinor().doubleValue(), 1d);
    }

    @Test
    public void testLoadBudgetsDetailData() {
        Date date = new Date();
        when(budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("1", "2", "3"))).thenReturn(Arrays.asList(createBudgetEntity()));
        when(workRecordRepository.getLatestWorkRecordDate(1l)).thenReturn(date);
        when(workRecordRepository.getSpentBudget(1l)).thenReturn(100000.0);
        when(planRecordRepository.getPlannedBudget(1l)).thenReturn(200000.0);
        when(workRecordRepository.getAverageDailyRate(1l)).thenReturn(50000.0);
        List<BudgetDetailData> data = budgetService.loadBudgetsDetailData(1l, new BudgetTagFilter(Arrays.asList("1", "2", "3"), 1l));
        Assert.assertEquals(1, data.size());
        Assert.assertEquals(100000.0d, data.get(0).getSpent().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(-100000.0d, data.get(0).getUnplanned().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(50000.0d, data.get(0).getAvgDailyRate().getAmountMinor().doubleValue(), 1d);
    }

    @Test
    public void testLoadBudgetToEdit() {
        BudgetEntity budget = createBudgetEntity();
        when(budgetRepository.findOne(1l)).thenReturn(budget);
        EditBudgetData data = budgetService.loadBudgetToEdit(1l);

        verify(budgetRepository, times(1)).findOne(1l);
        Assert.assertEquals(budget.getTotal(), data.getTotal());
        Assert.assertEquals(mapEntitiesToTags(budget.getTags()), data.getTags());
        Assert.assertEquals(budget.getImportKey(), data.getImportKey());
        Assert.assertEquals(budget.getName(), data.getTitle());
        Assert.assertEquals(budget.getId(), data.getId());
    }

    @Test
    public void testSaveBudget() {
        BudgetEntity budget = createBudgetEntity();
        when(budgetRepository.findOne(1l)).thenReturn(budget);

        EditBudgetData data = getEditBudgetEntity();

        budgetService.saveBudget(data);

        Assert.assertEquals(1l, budget.getId());
        Assert.assertEquals(data.getImportKey(), budget.getImportKey());
        Assert.assertEquals(data.getTags(), mapEntitiesToTags(budget.getTags()));
        Assert.assertEquals(data.getTitle(), budget.getName());
        Assert.assertEquals(data.getTotal(), budget.getTotal());
        Assert.assertEquals(data.getContract(), null);
    }

    @Test
    public void testSaveBudgetWithContract() {
        BudgetEntity budget = createBudgetEntity();
        when(budgetRepository.findOne(1l)).thenReturn(budget);

        ContractEntity contractEntity = createContract();
        when(contractRepository.findOne(1l)).thenReturn(contractEntity);

        EditBudgetData data = new EditBudgetData();
        data.setId(1l);
        ContractBaseData contractBaseData = new ContractBaseData();
        contractBaseData.setContractId(1l);
        data.setContract(contractBaseData);

        budgetService.saveBudget(data);

        Assert.assertEquals(1l, budget.getId());
        Assert.assertEquals(1l, budget.getContract().getId());
    }

    private ContractEntity createContract() {
        ContractEntity entity = new ContractEntity();
        entity.setId(1);
        entity.setName("TestName");
        entity.setBudgets(new LinkedList<BudgetEntity>());
        return entity;
    }

    private List<String> mapEntitiesToTags(List<BudgetTagEntity> tagEntities) {
        List<String> tags = new ArrayList<String>();
        for (BudgetTagEntity entity : tagEntities) {
            tags.add(entity.getTag());
        }
        return tags;
    }

    @Test
    public void testLoadBudgetUnits() {
        when(rateRepository.getDistinctRatesInCents(1l)).thenReturn(Arrays.asList(MoneyUtil.createMoney(100d), MoneyUtil.createMoney(200d)));
        List<Double> units = budgetService.loadBudgetUnits(1l);
        Assert.assertEquals(3, units.size());
        Assert.assertTrue(units.contains(1d));
        Assert.assertTrue(units.contains(100d));
        Assert.assertTrue(units.contains(200d));
    }

    @Test
    public void shouldThrowExceptionWhenConstraintIsViolated() {
        given(budgetRepository.save(Mockito.any(BudgetEntity.class)))
                .willThrow(new DataIntegrityViolationException("constraint violation"));
        when(budgetRepository.findOne(1L)).thenReturn(createBudgetEntity());
        try {
            budgetService.saveBudget(createBudgetEditEntity());
            Assert.fail("No Exception!");
        } catch (DataIntegrityViolationException e) {
            // yay
        }
    }

    private BudgetEntity createBudgetEntity() {
        BudgetEntity budget = new BudgetEntity();
        budget.setId(1l);
        budget.setTotal(MoneyUtil.createMoneyFromCents(100000));
        budget.setName("Budget 123");
        budget.getTags().add(new BudgetTagEntity("Tag1"));
        budget.getTags().add(new BudgetTagEntity("Tag2"));
        budget.getTags().add(new BudgetTagEntity("Tag3"));
        budget.setImportKey("budget123");
        return budget;
    }

    private EditBudgetData createBudgetEditEntity() {
        EditBudgetData data = new EditBudgetData();
        data.setId(1l);
        data.setImportKey("budget123");
        data.setTags(Arrays.asList("1", "2"));
        data.setTitle("title");
        data.setTotal(MoneyUtil.createMoneyFromCents(123));
        return data;
    }

    private EditBudgetData getEditBudgetEntity() {
        EditBudgetData data = new EditBudgetData();
        data.setId(1l);
        data.setImportKey("import");
        data.setTags(Arrays.asList("1", "2"));
        data.setTitle("title");
        data.setTotal(MoneyUtil.createMoneyFromCents(123));
        return data;
    }
}
