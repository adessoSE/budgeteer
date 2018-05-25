package org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbsPanelTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        BreadcrumbsPanel panel = new BreadcrumbsPanel("breadcrumbs", new DummyBreadcrumbsModel());
        tester.startComponentInPage(panel);
    }

    @Override
    protected void setupTest() {

    }
}

class DummyBreadcrumbsModel implements IModel<List<Breadcrumb>> {

    @Override
    public List<Breadcrumb> getObject() {
        final List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
        breadcrumbs.add(new Breadcrumb(DashboardPage.class, "Dashboard1"));
        breadcrumbs.add(new Breadcrumb(DashboardPage.class, "Dashboard2"));
        return breadcrumbs;
    }

    @Override
    public void setObject(List<Breadcrumb> object) {

    }

    @Override
    public void detach() {

    }
}