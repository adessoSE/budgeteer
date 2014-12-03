package org.wickedsource.budgeteer.service.record;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.mysema.query.types.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.ListUtil;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class WorkRecordQueriesIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private WorkRecordRepository repository;

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByEmptyFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1l);
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assert.assertEquals(4, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByPersonFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1l);
        filter.setPerson(new PersonBaseData(1l));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assert.assertEquals(2, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByBudgetFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1l);
        filter.setBudget(new BudgetBaseData(1l, "budget1"));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assert.assertEquals(2, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByDateFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1l);
        filter.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("15.08.2015")));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assert.assertEquals(3, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByMixedFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1l);
        filter.setPerson(new PersonBaseData(1l));
        filter.setBudget(new BudgetBaseData(1l, "budget1"));
        filter.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("02.01.2015")));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assert.assertEquals(2, records.size());
    }

}
