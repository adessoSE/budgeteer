package org.wickedsource.budgeteer.persistence.budget;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.RepositoryTestTemplate;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;

import java.util.Arrays;
import java.util.List;

public class BudgetRepositoryTest extends RepositoryTestTemplate {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DatabaseSetup("getAllTagsInProject.xml")
    @DatabaseTearDown(value = "getAllTagsInProject.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAllTagsInProject() {
        List<String> tags = budgetRepository.getAllTagsInProject(1l);
        Assert.assertEquals(4, tags.size());
        Assert.assertTrue(tags.contains("Tag 1"));
        Assert.assertTrue(tags.contains("Tag 2"));
        Assert.assertTrue(tags.contains("Tag 3"));
        Assert.assertTrue(tags.contains("Tag 4"));
    }

    @Test
    @DatabaseSetup("findByProjectId.xml")
    @DatabaseTearDown(value = "findByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByProjectId() {
        List<BudgetEntity> budgets = budgetRepository.findByProjectId(1l);
        Assert.assertEquals(1, budgets.size());
        Assert.assertEquals("Budget 1", budgets.get(0).getName());
    }

    @Test
    @DatabaseSetup("findByAtLeastOneTag.xml")
    @DatabaseTearDown(value = "findByAtLeastOneTag.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByAtLeastOneTag() {
        List<BudgetEntity> budgets = budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("Tag 1", "Tag 3"));
        Assert.assertEquals(2, budgets.size());
        List<BudgetEntity> budgets2 = budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("Tag 3"));
        Assert.assertEquals(1, budgets2.size());
    }

}
