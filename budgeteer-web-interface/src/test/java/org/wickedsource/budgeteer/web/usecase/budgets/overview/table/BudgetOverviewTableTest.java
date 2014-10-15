package org.wickedsource.budgeteer.web.usecase.budgets.overview.table;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.MoneyUtil;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.*;

import static org.mockito.Mockito.*;

public class BudgetOverviewTableTest extends AbstractWebTestTemplate {

    @Autowired
    private BudgetService service;

    private Random random = new Random();

    @Test
    public void render() {
        WicketTester tester = getTester();
        when(service.loadBudgetsDetailData(anyLong(), any(BudgetTagFilter.class))).thenReturn(createTestData());
        FilteredBudgetModel model = new FilteredBudgetModel(1, Model.of(new BudgetTagFilter()));
        BudgetOverviewTable table = new BudgetOverviewTable("table", model);
        tester.startComponentInPage(table);
    }

    public List<BudgetDetailData> createTestData() {
        List<BudgetDetailData> list = new ArrayList<BudgetDetailData>();
        for (int i = 0; i < 20; i++) {
            BudgetDetailData data = new BudgetDetailData();
            data.setLastUpdated(new Date());
            data.setName("Budget " + i);
            data.setSpent(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            data.setTotal(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            data.setTags(Arrays.asList("Active"));
        }
        return list;
    }

}
