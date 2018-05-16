package org.wickedsource.budgeteer.persistence.template;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.service.ReportType;

import java.util.List;

public class TemplateRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    TemplateRepository templateRepository;

    @Test
    public void testFindInProject() {
        addTemplateToProject(1L);

        List<TemplateEntity> templatesInProject = templateRepository.findByProjectId(1L);
        Assert.assertEquals(1, templatesInProject.size());
        Assert.assertEquals("template1", templatesInProject.get(0).getName());
    }

    @Test
    public void deleteTemplateTest() {
        TemplateEntity templateEntity = new TemplateEntity("template1", "desc1", ReportType.BUDGET_REPORT, new XSSFWorkbook(), 1L);
        templateRepository.save(templateEntity);
        templateRepository.delete(templateEntity.getId());
        Assert.assertEquals(0, templateRepository.findByProjectId(1L).size());
    }

    @Test
    public void deleteAllTest() {
        addTemplateToProject(1L);
        templateRepository.deleteAll();
        Assert.assertEquals(0, templateRepository.findByProjectId(1L).size());
    }

    private void addTemplateToProject(long projectId){
        templateRepository.save(new TemplateEntity("template1", "desc1", ReportType.BUDGET_REPORT, new XSSFWorkbook(), projectId));
    }
}
