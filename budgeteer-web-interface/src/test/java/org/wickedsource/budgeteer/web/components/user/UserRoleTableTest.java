package org.wickedsource.budgeteer.web.components.user;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;

public class UserRoleTableTest extends AbstractWebTestTemplate {
    @Test
    void render() {
        WicketTester tester = getTester();
        CustomFeedbackPanel feedback = new CustomFeedbackPanel("feedback");
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1);
        UserRoleTable table = new UserRoleTable("table", 1L, feedback, ProjectAdministrationPage.class,
                ProjectAdministrationPage.class, ProjectAdministrationPage.class, parameters);

        tester.startComponentInPage(table);
    }

    @Override
    protected void setupTest() {

    }
}