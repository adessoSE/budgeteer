package org.wickedsource.budgeteer.web.pages.dashboard;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.administration.Project;

import java.util.Date;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class DashboardPageTest extends AbstractWebTestTemplate {

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        //TODO Alle Services brauchen Mocks in der AbstractWebTestTemplate-Klasse
        tester.startPage(DashboardPage.class);
        tester.assertRenderedPage(DashboardPage.class);
    }

    @Override
    protected void setupTest() {
    }
}
