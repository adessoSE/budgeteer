package org.wickedsource.budgeteer.web.pages.template.templateimport;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.ImportTemplatesPage;

import java.net.URISyntaxException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ImportTemplatesPageTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        tester.startPage(new ImportTemplatesPage(TemplatesPage.class, new PageParameters()));
        tester.assertRenderedPage(ImportTemplatesPage.class);
    }

    @Test
    void testBacklink1Click() {
        WicketTester tester = getTester();
        tester.startPage(new ImportTemplatesPage(TemplatesPage.class, new PageParameters()));
        tester.clickLink("backlink1");
        tester.assertRenderedPage(TemplatesPage.class);
    }

    @Test
    void testBacklink2Click() {
        WicketTester tester = getTester();
        tester.startPage(new ImportTemplatesPage(TemplatesPage.class, new PageParameters()));
        tester.clickLink("importForm:backlink2");
        tester.assertRenderedPage(TemplatesPage.class);
    }

    @Test
    void NoDataFormSubmitTest() {
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", false);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.submit();
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertErrorMessages("Please select a file to import");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void NameAndDescriptionFormSubmitTest() {
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", false);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        formTester.submit();
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertErrorMessages("Please select a file to import");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void NoNameNoDescriptionWithFileTest() { //yes I know this is a horrible name
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setFile("fileUpload", getExampleTemplate(), "xlsx");
        formTester.select("type", 1);
        tester.executeAjaxEvent("importForm:save", "onclick");
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertErrorMessages("You have not entered a name.");

        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    public void NoTypeTest() {
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setFile("fileUpload", getExampleTemplate(), "xlsx");
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        tester.executeAjaxEvent("importForm:save", "onclick");
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertErrorMessages("You have not selected a type");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    public void InvalidFileTest() {
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setFile("fileUpload", getInvalidExampleTemplate(), "txt");
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        formTester.select("type", 1);
        tester.executeAjaxEvent("importForm:save", "onclick");
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertErrorMessages("The file did not match the expected file format. Please choose a .xls- or a .xlsx-file.");
    }

    @Test
    void AllCorrectInputForm() {
        WicketTester tester = getTester();
        ImportTemplatesPage testPage = new ImportTemplatesPage(TemplatesPage.class, new PageParameters());
        tester.startPage(testPage);
        Assertions.assertNotNull(testPage.get("importForm"));
        FormTester formTester = tester.newFormTester("importForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setFile("fileUpload", getExampleTemplate(), "xlsx");
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        formTester.select("type", 1);
        tester.executeAjaxEvent("importForm:save", "onclick");
        tester.assertRenderedPage(ImportTemplatesPage.class);
        tester.assertNoErrorMessage();
        tester.assertFeedbackMessages(null, "The selected files were imported successfully");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    private File getExampleTemplate() {
        try {
            return new File(getClass().getResource("exampleTemplate1.xlsx").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File getInvalidExampleTemplate() {
        try {
            return new File(getClass().getResource("invalidTemplate.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setupTest() {

    }
}
