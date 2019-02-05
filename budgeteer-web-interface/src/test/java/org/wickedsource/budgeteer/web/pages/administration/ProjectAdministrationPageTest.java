package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.ArgumentMatchers.any;

public class ProjectAdministrationPageTest extends AbstractWebTestTemplate {

    @Test
    void renderTest() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        tester.assertRenderedPage(ProjectAdministrationPage.class);
    }

    @Test
    void testFormSubmitError() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        FormTester formTester = tester.newFormTester("projectChangeForm");
        formTester.setValue("projectTitle", "");
        tester.executeAjaxEvent("projectChangeForm:projectTitle", "change");
        formTester.setValue("projectStart", "01.01.2019 - 28.02.2019");
        tester.executeAjaxEvent("projectChangeForm:projectStart", "change");
        tester.clickLink("projectChangeForm:submitLink");
        tester.assertRenderedPage(ProjectAdministrationPage.class);
        tester.assertFeedbackMessages(FeedbackMessage::isError, tester.getLastRenderedPage().getString("error.no.name"));
        Mockito.verify(projectServiceMock, Mockito.times(0)).save(any());
    }

    @Test
    void testFormSubmitSuccess() {
        WicketTester tester = getTester();
        tester.startPage(ProjectAdministrationPage.class);
        FormTester formTester = tester.newFormTester("projectChangeForm");
        formTester.setValue("projectTitle", "test");
        tester.executeAjaxEvent("projectChangeForm:projectTitle", "change");
        formTester.setValue("projectStart", "01.01.2019 - 28.02.2019");
        tester.executeAjaxEvent("projectChangeForm:projectStart", "change");
        tester.clickLink("projectChangeForm:submitLink");
        tester.assertRenderedPage(ProjectAdministrationPage.class);
        tester.assertFeedbackMessages(FeedbackMessage::isSuccess, tester.getLastRenderedPage().getString("project.saved"));
        Mockito.verify(projectServiceMock, Mockito.times(1)).save(any());
    }

    @Override
    protected void setupTest() {

    }
}