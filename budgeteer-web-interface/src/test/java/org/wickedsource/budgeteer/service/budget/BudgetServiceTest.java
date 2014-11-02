package org.wickedsource.budgeteer.service.budget;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class BudgetServiceTest extends ServiceTestTemplate {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetService budgetService;

    @Test
    public void testLoadBudgetBaseDataForProject() throws Exception {
        when(budgetRepository.findByProjectId(1l)).thenReturn(Arrays.asList(createBudgetEntity()));
        List<BudgetBaseData> budgets = budgetService.loadBudgetBaseDataForProject(1l);
        Assert.assertEquals(1, budgets.size());
        Assert.assertEquals(1, budgets.get(0).getId());
        Assert.assertEquals("Budget 123", budgets.get(0).getName());
    }

    @Test
    public void testLoadBudgetBaseData() throws Exception {
        when(budgetRepository.findOne(1l)).thenReturn(createBudgetEntity());
        BudgetBaseData data = budgetService.loadBudgetBaseData(1l);
        Assert.assertEquals(1l, data.getId());
        Assert.assertEquals("Budget 123", data.getName());
    }

    @Test
    public void testLoadBudgetTags() throws Exception {
        Set<String> tags = new HashSet<String>();
        tags.add("1");
        tags.add("2");
        when(budgetRepository.getAllTagsInProject(1l)).thenReturn(tags);
        List<String> loadedTags = budgetService.loadBudgetTags(1l);
        Assert.assertEquals(2, loadedTags.size());
        Assert.assertTrue(loadedTags.contains("1"));
        Assert.assertTrue(loadedTags.contains("2"));
    }

    @Test
    public void testLoadBudgetDetailData() throws Exception {
        Assert.fail();
    }

    @Test
    public void testLoadBudgetsDetailData() throws Exception {
        Assert.fail();
    }

    @Test
    public void testLoadBudgetToEdit() throws Exception {
        BudgetEntity budget = createBudgetEntity();
        when(budgetRepository.findOne(1l)).thenReturn(budget);
        EditBudgetData data = budgetService.loadBudgetToEdit(1l);

        verify(budgetRepository, times(1)).findOne(1l);
        Assert.assertEquals(budget.getTotal(), data.getTotal());
        Assert.assertEquals(budget.getTags(), data.getTags());
        Assert.assertEquals(budget.getImportKey(), data.getImportKey());
        Assert.assertEquals(budget.getName(), data.getTitle());
        Assert.assertEquals(budget.getId(), data.getId());
    }

    @Test
    public void testEditBudget() throws Exception {
        BudgetEntity budget = createBudgetEntity();
        when(budgetRepository.findOne(1l)).thenReturn(budget);

        EditBudgetData data = new EditBudgetData();
        data.setId(1l);
        data.setImportKey("import");
        data.setTags(Arrays.asList("1", "2"));
        data.setTitle("title");
        data.setTotal(MoneyUtil.createMoneyFromCents(123));

        budgetService.editBudget(data);

        Assert.assertEquals(1l, budget.getId());
        Assert.assertEquals(data.getImportKey(), budget.getImportKey());
        Assert.assertEquals(data.getTags(), budget.getTags());
        Assert.assertEquals(data.getTitle(), budget.getName());
        Assert.assertEquals(data.getTotal(), budget.getTotal());
    }

    @Test
    public void testLoadBudgetUnits() throws Exception {
        Assert.fail();
    }

    private BudgetEntity createBudgetEntity() {
        BudgetEntity budget = new BudgetEntity();
        budget.setId(1l);
        budget.setTotal(MoneyUtil.createMoneyFromCents(100000));
        budget.setName("Budget 123");
        budget.setTags(Arrays.asList("Tag1", "Tag2", "Tag3"));
        budget.setImportKey("budget123");
        return budget;
    }
}
