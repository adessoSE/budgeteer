package org.wickedsource.budgeteer.web.pages.template.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.edit.EditTemplatePage;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class EditTemplatePageTest extends AbstractWebTestTemplate {

  @Test
  void testRender() {
    WicketTester tester = getTester();
    tester.startPage(
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1)));
    tester.assertRenderedPage(EditTemplatePage.class);
  }

  @Test
  void testBacklink1Click() {
    WicketTester tester = getTester();
    tester.startPage(
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1)));
    tester.clickLink("backlink1");
    tester.assertRenderedPage(TemplatesPage.class);
  }

  @Test
  void testBacklink2Click() {
    WicketTester tester = getTester();
    tester.startPage(
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1)));
    tester.clickLink("editForm:backlink2");
    tester.assertRenderedPage(TemplatesPage.class);
  }

  @Test
  void NoDataFormSubmitTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("editForm"));
    FormTester formTester = tester.newFormTester("editForm", true);
    formTester.setClearFeedbackMessagesBeforeSubmit(true);
    formTester.setFile("fileUpload", null, "");
    tester.executeAjaxEvent("editForm:save", "click");
    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertErrorMessages("You have not entered a name.", "You have not selected a type");
    assertThat(tester.getFeedbackMessages(null)).hasSize(2);
  }

  @Test
  void AllCorrectInputForm() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("editForm"));
    FormTester formTester = tester.newFormTester("editForm", true);
    formTester.setValue("name", "TEST_N");
    formTester.setValue("description", "TEST_D");
    formTester.select("type", 1);
    formTester.setFile("fileUpload", null, "");
    tester.executeAjaxEvent("editForm:save", "click");
    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertNoErrorMessage();
    tester.assertFeedbackMessages(null, "The template was edited successfully");
    assertThat(tester.getFeedbackMessages(null)).hasSize(1);
  }

  @Test
  void NoTypeTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("editForm"));
    FormTester formTester = tester.newFormTester("editForm", true);
    formTester.setClearFeedbackMessagesBeforeSubmit(true);
    formTester.setValue("name", "TEST_N");
    formTester.setValue("description", "TEST_D");
    tester.executeAjaxEvent("editForm:save", "click");
    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertErrorMessages("You have not selected a type");
    assertThat(tester.getFeedbackMessages(null)).hasSize(1);
  }

  @Test
  void DeleteButtonTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("editForm:deleteButton"));
    tester.clickLink("editForm:deleteButton");
    tester.assertRenderedPage(DeleteDialog.class);
  }

  @Test
  void testRedirectOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditTemplatePage.class, new PageParameters());
    tester.assertRenderedPage(TemplatesPage.class);
  }

  @Test
  void testRedirectOnWrongId() {
    WicketTester tester = getTester();
    tester.startPage(EditTemplatePage.class, TemplatesPage.createParameters(8L));
    tester.assertRenderedPage(DashboardPage.class);
  }

  @Override
  public void setupTest() {}
}
