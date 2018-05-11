package org.wickedsource.budgeteer.service.template;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class TemplateServiceTest {

    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateRepository templateRepository;

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void doImportTest() {
        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        templateService.doImport(1, new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")),
                model(from(testDto)));
        Assert.assertEquals(1, templateService.getTemplatesInProject(1).size());
        Assert.assertEquals(testDto.getName(), templateService.getTemplatesInProject(1).get(0).getName());
        Assert.assertEquals("TEST_D", templateService.getTemplatesInProject(1).get(0).getDescription());
        Assert.assertNotNull(templateService.getTemplates().get(0).getWb());
    }

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void editTemplateTest() {
        populateDatabase(2);
        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        Assert.assertEquals(2, templateService.getTemplatesInProject(1).size());

        long newId = templateService.editTemplate(1, templateService.getTemplatesInProject(1).get(0).getId(),
                new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")), model(from(testDto)));

        Assert.assertEquals("TEST", templateService.getById(newId).getName());
        Assert.assertEquals("TEST_D", templateService.getById(newId).getDescription());
        Assert.assertNotNull(templateService.getById(newId).getWb());
    }

    @Test
    @DatabaseSetup("templateTest.xml")
    @DatabaseTearDown(value = "templateTest.xml", type = DatabaseOperation.DELETE_ALL)
    public void deleteTemplateTest() {
        populateDatabase(1);
        Assert.assertEquals(1, templateService.getTemplatesInProject(1).size());
        Template testTempl = templateService.getTemplates().get(0);
        templateService.deleteTemplate(testTempl.getId());
        Assert.assertEquals(0, templateService.getTemplates().size());
        Assert.assertNull(templateService.getById(testTempl.getId()));
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
    public void getExampleFileTest(){
        try {
            XSSFWorkbook testWorkbok = (XSSFWorkbook)WorkbookFactory.create(templateService.getExampleFile().getInputStream());
            Assert.assertNotNull(testWorkbok);
        }catch (IOException | InvalidFormatException e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Before
    public void emptyDatabase(){
        templateRepository.deleteAll();
    }
}

