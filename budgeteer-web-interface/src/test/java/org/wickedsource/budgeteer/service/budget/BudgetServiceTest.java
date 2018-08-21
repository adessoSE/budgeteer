package org.wickedsource.budgeteer.service.budget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

class BudgetServiceTest extends ServiceTestTemplate {

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
    void testLoadBudgetBaseDataForProject() {
        when(budgetRepository.findByProjectIdOrderByNameAsc(1L)).thenReturn(Collections.singletonList(createBudgetEntity().get()));
        List<BudgetBaseData> budgets = budgetService.loadBudgetBaseDataForProject(1L);
        Assertions.assertEquals(1, budgets.size());
        Assertions.assertEquals(1, budgets.get(0).getId());
        Assertions.assertEquals("Budget 123", budgets.get(0).getName());
    }

    @Test
    void testLoadBudgetBaseData() {
        when(budgetRepository.findById(1L)).thenReturn(createBudgetEntity());
        BudgetBaseData data = budgetService.loadBudgetBaseData(1L);
        Assertions.assertEquals(1L, data.getId());
        Assertions.assertEquals("Budget 123", data.getName());
    }

    @Test
    void testLoadBudgetTags() {
        List<String> tags = new ArrayList<String>();
        tags.add("1");
        tags.add("2");
        when(budgetRepository.getAllTagsInProject(1L)).thenReturn(tags);
        List<String> loadedTags = budgetService.loadBudgetTags(1L);
        Assertions.assertEquals(2, loadedTags.size());
        Assertions.assertTrue(loadedTags.contains("1"));
        Assertions.assertTrue(loadedTags.contains("2"));
    }

    @Test
    void testLoadBudgetDetailData() {
        Date date = new Date();
        when(budgetRepository.findById(1L)).thenReturn(createBudgetEntity());
        when(workRecordRepository.getLatestWorkRecordDate(1L)).thenReturn(date);
        when(workRecordRepository.getSpentBudget(1L)).thenReturn(100000.0);
        when(planRecordRepository.getPlannedBudget(1L)).thenReturn(200000.0);
        when(workRecordRepository.getAverageDailyRate(1L)).thenReturn(50000.0);
        BudgetDetailData data = budgetService.loadBudgetDetailData(1L);
        Assertions.assertEquals(100000.0d, data.getSpent().getAmountMinor().doubleValue(), 1d);
        Assertions.assertEquals(-100000.0d, data.getUnplanned().getAmountMinor().doubleValue(), 1d);
        Assertions.assertEquals(50000.0d, data.getAvgDailyRate().getAmountMinor().doubleValue(), 1d);
    }

    @Test
    void testLoadBudgetsDetailData() {
        Date date = new Date();
        when(budgetRepository.findByAtLeastOneTag(1L, Arrays.asList("1", "2", "3"))).thenReturn(Collections.singletonList(createBudgetEntity().get()));
        when(workRecordRepository.getLatestWorkRecordDate(1L)).thenReturn(date);
        when(workRecordRepository.getSpentBudget(1L)).thenReturn(100000.0);
        when(planRecordRepository.getPlannedBudget(1L)).thenReturn(200000.0);
        when(workRecordRepository.getAverageDailyRate(1L)).thenReturn(50000.0);
        List<BudgetDetailData> data = budgetService.loadBudgetsDetailData(1L, new BudgetTagFilter(Arrays.asList("1", "2", "3"), 1L));
        Assertions.assertEquals(1, data.size());
        Assertions.assertEquals(100000.0d, data.get(0).getSpent().getAmountMinor().doubleValue(), 1d);
        Assertions.assertEquals(-100000.0d, data.get(0).getUnplanned().getAmountMinor().doubleValue(), 1d);
        Assertions.assertEquals(50000.0d, data.get(0).getAvgDailyRate().getAmountMinor().doubleValue(), 1d);
    }

    @Test
    void testLoadBudgetToEdit() {
        Optional<BudgetEntity> budget = createBudgetEntity();
        when(budgetRepository.findById(1L)).thenReturn(budget);
        EditBudgetData data = budgetService.loadBudgetToEdit(1L);
        if(budget.isPresent()) {
            verify(budgetRepository, times(1)).findById(1L);
            Assertions.assertEquals(budget.get().getTotal(), data.getTotal());
            Assertions.assertEquals(mapEntitiesToTags(budget.get().getTags()), data.getTags());
            Assertions.assertEquals(budget.get().getImportKey(), data.getImportKey());
            Assertions.assertEquals(budget.get().getName(), data.getTitle());
            Assertions.assertEquals(budget.get().getId(), data.getId());
            Assertions.assertEquals(budget.get().getNote(), data.getNote());
        }
    }

    @Test
    void testSaveBudget() {
        Optional<BudgetEntity> budget = createBudgetEntity();
        when(budgetRepository.findById(1L)).thenReturn(budget);

        EditBudgetData data = getEditBudgetEntity();

        budgetService.saveBudget(data);
        if(budget.isPresent()) {
            Assertions.assertEquals(1L, budget.get().getId());
            Assertions.assertEquals(data.getImportKey(), budget.get().getImportKey());
            Assertions.assertEquals(data.getTags(), mapEntitiesToTags(budget.get().getTags()));
            Assertions.assertEquals(data.getTitle(), budget.get().getName());
            Assertions.assertEquals(data.getTotal(), budget.get().getTotal());
            Assertions.assertEquals(data.getNote(), budget.get().getNote());
            Assertions.assertNull(data.getContract());
        }
    }

    @Test
    void testSaveBudgetWithContract() {
        Optional<BudgetEntity> budget = createBudgetEntity();
        when(budgetRepository.findById(1L)).thenReturn(budget);

        Optional<ContractEntity> contractEntity = createContract();
        when(contractRepository.findById(1L)).thenReturn(contractEntity);

        EditBudgetData data = new EditBudgetData();
        data.setId(1L);
        ContractBaseData contractBaseData = new ContractBaseData();
        contractBaseData.setContractId(1L);
        data.setContract(contractBaseData);

        budgetService.saveBudget(data);

        Assertions.assertEquals(1L, budget.get().getId());
        Assertions.assertEquals(1L, budget.get().getContract().getId());
    }

    private Optional<ContractEntity> createContract() {
        ContractEntity entity = new ContractEntity();
        entity.setId(1);
        entity.setName("TestName");
        entity.setBudgets(new LinkedList<>());
        return Optional.of(entity);
    }

    private List<String> mapEntitiesToTags(List<BudgetTagEntity> tagEntities) {
        List<String> tags = new ArrayList<>();
        for (BudgetTagEntity entity : tagEntities) {
            tags.add(entity.getTag());
        }
        return tags;
    }

    @Test
    void testLoadBudgetUnits() {
        when(rateRepository.getDistinctRatesInCents(1L)).thenReturn(Arrays.asList(MoneyUtil.createMoney(100d), MoneyUtil.createMoney(200d)));
        List<Double> units = budgetService.loadBudgetUnits(1L);
        Assertions.assertEquals(3, units.size());
        Assertions.assertTrue(units.contains(1d));
        Assertions.assertTrue(units.contains(100d));
        Assertions.assertTrue(units.contains(200d));
    }

    @Test
    void shouldThrowExceptionWhenConstraintIsViolated() {
        given(budgetRepository.save(Mockito.any(BudgetEntity.class)))
                .willThrow(new DataIntegrityViolationException("constraint violation"));
        when(budgetRepository.findById(1L)).thenReturn(createBudgetEntity());
        try {
            budgetService.saveBudget(createBudgetEditEntity());
            Assertions.fail("No Exception!");
        } catch (DataIntegrityViolationException e) {
            // yay
        }
    }

    private Optional<BudgetEntity> createBudgetEntity() {
        BudgetEntity budget = new BudgetEntity();
        budget.setId(1L);
        budget.setTotal(MoneyUtil.createMoneyFromCents(100000));
        budget.setName("Budget 123");
        budget.getTags().add(new BudgetTagEntity("Tag1"));
        budget.getTags().add(new BudgetTagEntity("Tag2"));
        budget.getTags().add(new BudgetTagEntity("Tag3"));
        budget.setImportKey("budget123");
        return Optional.of(budget);
    }

    private EditBudgetData createBudgetEditEntity() {
        EditBudgetData data = new EditBudgetData();
        data.setId(1L);
        data.setImportKey("budget123");
        data.setTags(Arrays.asList("1", "2"));
        data.setTitle("title");
        data.setTotal(MoneyUtil.createMoneyFromCents(123));
        return data;
    }

    private EditBudgetData getEditBudgetEntity() {
        EditBudgetData data = new EditBudgetData();
        data.setId(1L);
        data.setImportKey("import");
        data.setTags(Arrays.asList("1", "2"));
        data.setTitle("title");
        data.setTotal(MoneyUtil.createMoneyFromCents(123));
        return data;
    }
}
