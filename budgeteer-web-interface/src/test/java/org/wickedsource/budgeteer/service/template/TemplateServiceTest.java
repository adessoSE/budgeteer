package org.wickedsource.budgeteer.service.template;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.wickedsource.budgeteer.IntegrationTestConfiguration;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.persistence.template.TemplateRepository;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

import java.io.IOException;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
class TemplateServiceTest {

    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateRepository templateRepository;

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    void doImportTest() {
        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        templateService.doImport(1, new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")),
                model(from(testDto)));
        Assertions.assertEquals(1, templateService.getTemplatesInProject(1).size());
        Assertions.assertEquals(testDto.getName(), templateService.getTemplatesInProject(1).get(0).getName());
        Assertions.assertEquals("TEST_D", templateService.getTemplatesInProject(1).get(0).getDescription());
        Assertions.assertNotNull(templateService.getTemplates().get(0).getWb());
    }

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    void editTemplateTest() {
        populateDatabase(2);
        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        Assertions.assertEquals(2, templateService.getTemplatesInProject(1).size());

        long newId = templateService.editTemplate(1, templateService.getTemplatesInProject(1).get(0).getId(),
                new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")), model(from(testDto)));

        Assertions.assertEquals("TEST", templateService.getById(newId).getName());
        Assertions.assertEquals("TEST_D", templateService.getById(newId).getDescription());
        Assertions.assertNotNull(templateService.getById(newId).getWb());
    }

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    void deleteTemplateTest() {
        populateDatabase(1);
        Assertions.assertEquals(1, templateService.getTemplatesInProject(1).size());
        Template testTempl = templateService.getTemplates().get(0);
        templateService.deleteTemplate(testTempl.getId());
        Assertions.assertEquals(0, templateService.getTemplates().size());
        Assertions.assertNull(templateService.getById(testTempl.getId()));
    }

    /**
     * Inserts a give amount of templates into the database.
     * @param amount The amount of templates to create
     */
    private void populateDatabase(int amount){
        for(int i = 0; i < amount; i++){
            templateService.doImport(1, new ImportFile("exampleTemplate1.xlsx",
                            getClass().getResourceAsStream("exampleTemplate1.xlsx")),
                    model(from(new TemplateFormInputDto(1))));
        }
    }

    @Test
    void getExampleFileTest(){
        try {
            XSSFWorkbook testWorkbok = (XSSFWorkbook)WorkbookFactory.create(templateService.getExampleFile().getInputStream());
            Assertions.assertNotNull(testWorkbok);
        }catch (IOException | InvalidFormatException e){
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @BeforeEach
    void emptyDatabase(){
        templateRepository.deleteAll();
    }
}

