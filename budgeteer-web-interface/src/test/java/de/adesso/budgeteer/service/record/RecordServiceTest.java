package de.adesso.budgeteer.service.record;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.imports.ImportEntity;
import de.adesso.budgeteer.persistence.person.PersonEntity;
import de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import de.adesso.budgeteer.service.ServiceTestTemplate;
import de.adesso.budgeteer.service.budget.BudgetTagFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = {"classpath:spring-service.xml", "classpath:spring-repository-mock.xml"})
class RecordServiceTest extends ServiceTestTemplate {

    private static final List<String> EMPTY_STRING_LIST = new ArrayList<>(0);

    @Autowired
    @ReplaceWithMock
    private RecordJoiner recordJoiner;

    @Autowired
    @ReplaceWithMock
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private RecordService service;

    @Test
    void testGetWeeklyAggregationForPerson() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForPerson(1L);
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetMonthlyAggregationForPerson() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinMonthly(anyListOf(MonthlyAggregatedRecordBean.class), anyListOf(MonthlyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getMonthlyAggregationForPerson(1L);
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetWeeklyAggregationForBudget() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudget(1L);
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetMonthlyAggregationForBudget() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudget(1L);
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetWeeklyAggregationForBudgets() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudgets(new BudgetTagFilter(EMPTY_STRING_LIST, 1L));
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetMonthlyAggregationForBudgets() throws Exception {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinMonthly(anyListOf(MonthlyAggregatedRecordBean.class), anyListOf(MonthlyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getMonthlyAggregationForBudgets(new BudgetTagFilter(EMPTY_STRING_LIST, 1L));
        Assertions.assertEquals(recordList, resultList);
    }

    @Test
    void testGetFilteredRecords() throws Exception {
        List<WorkRecordEntity> recordList = createRecordList();
        when(workRecordRepository.findAll(any(Predicate.class))).thenReturn(recordList);
        List<WorkRecord> filteredRecords = service.getFilteredRecords(new WorkRecordFilter(1L));
        Assertions.assertEquals(recordList.size(), filteredRecords.size());
        Assertions.assertEquals(WorkRecord.class, filteredRecords.get(0).getClass());
    }

    private List<WorkRecordEntity> createRecordList() {
        List<WorkRecordEntity> list = new ArrayList<>();
        WorkRecordEntity record = new WorkRecordEntity();
        record.setDailyRate(MoneyUtil.createMoney(100d));
        record.setDate(new Date());
        record.setId(1L);
        record.setBudget(new BudgetEntity());
        record.setImportRecord(new ImportEntity());
        record.setMinutes(480);
        record.setPerson(new PersonEntity());
        list.add(record);
        return list;
    }


    private List<AggregatedRecord> createAggregatedRecordList() {
        List<AggregatedRecord> list = new ArrayList<>();
        list.add(new AggregatedRecord());
        list.add(new AggregatedRecord());
        list.add(new AggregatedRecord());
        return list;
    }
}
