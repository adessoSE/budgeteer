package org.wickedsource.budgeteer.web.pages.template.edit;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.edit.EditTemplatePage;

public class EditTemplatePageTest extends AbstractWebTestTemplate {

  @Autowired @ReplaceWithMock private TemplateService templateService;

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
    tester.clickLink("form:backlink2");
    tester.assertRenderedPage(TemplatesPage.class);
  }

  @Test
  void NoDataFormSubmitTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("form"));
    FormTester formTester = tester.newFormTester("form:editTemplate:form", true);
    formTester.setClearFeedbackMessagesBeforeSubmit(true);
    formTester.setFile("fileUpload", null, "");
    formTester.submit();
    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertErrorMessages("'name' is required.", "'type' is required.");
    assertThat(tester.getFeedbackMessages(null)).hasSize(2);
  }

  @Test
  void AllCorrectInputForm() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("form"));
    FormTester formTester = tester.newFormTester("form", true);
    formTester.setValue("editTemplate:form:name", "TEST_N");
    formTester.setValue("editTemplate:form:description", "TEST_D");
    formTester.select("editTemplate:form:type", 1);
    formTester.setFile("editTemplate:form:fileUpload", null, "");
    formTester.submit();

    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertNoErrorMessage();
  }

  @Test
  public void NoTypeTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("form"));
    FormTester formTester = tester.newFormTester("form:editTemplate:form", true);
    formTester.setClearFeedbackMessagesBeforeSubmit(true);
    formTester.setValue("name", "TEST_N");
    formTester.setValue("description", "TEST_D");
    formTester.setValue("type", "");
    formTester.submit();
    tester.assertRenderedPage(EditTemplatePage.class);
    tester.assertErrorMessages("'type' is required.");
    assertThat(tester.getFeedbackMessages(null)).hasSize(1);
  }

  @Test
  public void DeleteButtonTest() {
    WicketTester tester = getTester();
    EditTemplatePage testPage =
        new EditTemplatePage(
            TemplatesPage.class, new PageParameters(), TemplatesPage.createParameters(1));
    tester.startPage(testPage);
    Assertions.assertNotNull(testPage.get("form:deleteButton"));
    tester.clickLink("form:deleteButton");
    tester.assertRenderedPage(DeleteDialog.class);
  }

  @Test
  public void testRedirectOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditTemplatePage.class, new PageParameters());
    tester.assertRenderedPage(TemplatesPage.class);
  }

  @Test
  public void testRedirectOnWrongId() {
    WicketTester tester = getTester();
    tester.startPage(EditTemplatePage.class, TemplatesPage.createParameters(8L));
    tester.assertRenderedPage(DashboardPage.class);
  }

  @Override
  public void setupTest() {
    when(templateService.getById(1L))
        .thenReturn(new Template(1L, null, null, null, null, false, 1L));
    when(templateService.getById(8L)).thenReturn(null);
  }
}
