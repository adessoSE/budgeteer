package org.wickedsource.budgeteer.persistence.budget;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.wickedsource.budgeteer.persistence.RepositoryTestTemplate;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BudgetRepositoryTest extends RepositoryTestTemplate {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @Rollback
    public void testGetAllTagsInProject() {
        createBudgetsWithProject(1l);
        Set<String> tags = budgetRepository.getAllTagsInProject(1l);
        Assert.assertEquals(4, tags.size());
        Assert.assertTrue(tags.contains("1"));
        Assert.assertTrue(tags.contains("2"));
        Assert.assertTrue(tags.contains("3"));
        Assert.assertTrue(tags.contains("4"));
    }

    @Test
    @Rollback
    public void testFindByProjectId() {
        createBudgetsWithProject(2l);
        List<BudgetEntity> budgets = budgetRepository.findByProjectId(2l);
        Assert.assertEquals(2, budgets.size());
    }

    private void createBudgetsWithProject(long projectId) {
        ProjectEntity project = createProjectEntity(projectId);
        projectRepository.save(project);
        BudgetEntity budget1 = new BudgetEntity();
        budget1.setName("budget1");
        budget1.setImportKey("budget1");
        budget1.getTags().addAll(Arrays.asList("1", "4", "3"));
        budget1.setProject(project);
        budgetRepository.save(budget1);
        BudgetEntity budget2 = new BudgetEntity();
        budget2.setName("budget2");
        budget2.setImportKey("budget2");
        budget2.getTags().addAll(Arrays.asList("1", "2", "3"));
        budget2.setProject(project);
        budgetRepository.save(budget2);
    }


    private ProjectEntity createProjectEntity(long projectId) {
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setName("name");
        return project;
    }

}
