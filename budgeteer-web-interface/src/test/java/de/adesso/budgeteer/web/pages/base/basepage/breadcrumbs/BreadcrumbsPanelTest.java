package de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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