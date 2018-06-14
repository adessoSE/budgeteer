package org.wickedsource.budgeteer.web.pages.budgets.overview.table;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

public class BudgetOverviewTableTest extends AbstractWebTestTemplate {

    private static final List<String> EMPTY_STRING_LIST = new ArrayList<>(0);

    @Autowired
    private BudgetService service;

    private Random random = new Random();

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadBudgetsDetailData(anyLong(), any(BudgetTagFilter.class))).thenReturn(createTestData());
        FilteredBudgetModel model = new FilteredBudgetModel(1, Model.of(new BudgetTagFilter(EMPTY_STRING_LIST, 1L)));
        BudgetOverviewTable table = new BudgetOverviewTable("table", model, null);
        tester.startComponentInPage(table);
    }

    private List<BudgetDetailData> createTestData() {
        List<BudgetDetailData> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BudgetDetailData data = new BudgetDetailData();
            data.setLastUpdated(new Date());
            data.setName("Budget " + i);
            data.setSpent(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            data.setTotal(MoneyUtil.createMoneyFromCents(random.nextInt(5000)));
            data.setTags(singletonList("Active"));
        }
        return list;
    }

    @Override
    protected void setupTest() {

    }
}
