package org.wickedsource.budgeteer.persistence.template;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.util.List;

class TemplateRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    TemplateRepository templateRepository;

    @Test
    void testFindInProject() {
        addTemplateToProject(1L);

        List<TemplateEntity> templatesInProject = templateRepository.findByProjectId(1L);
        Assertions.assertEquals(1, templatesInProject.size());
        Assertions.assertEquals("template1", templatesInProject.get(0).getName());
    }

    @Test
    void deleteTemplateTest() {
        TemplateEntity templateEntity = new TemplateEntity("template1", "desc1", new XSSFWorkbook(), 1L);
        templateRepository.save(templateEntity);
        templateRepository.delete(templateEntity.getId());
        Assertions.assertEquals(0, templateRepository.findByProjectId(1L).size());
    }

    @Test
    void deleteAllTest() {
        addTemplateToProject(1L);
        templateRepository.deleteAll();
        Assertions.assertEquals(0, templateRepository.findByProjectId(1L).size());
    }

    private void addTemplateToProject(long projectId){
        templateRepository.save(new TemplateEntity("template1", "desc1", new XSSFWorkbook(), projectId));
    }
}
