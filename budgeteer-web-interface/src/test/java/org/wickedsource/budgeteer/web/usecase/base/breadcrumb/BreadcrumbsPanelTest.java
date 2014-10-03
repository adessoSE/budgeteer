package org.wickedsource.budgeteer.web.usecase.base.breadcrumb;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.Breadcrumb;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsPanel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbsPanelTest {

    @Test
    public void testRender() {

        WicketTester tester = new WicketTester();
        BreadcrumbsPanel panel = new BreadcrumbsPanel("breadcrumbs", new DummyBreadcrumbsModel());
        tester.startComponentInPage(panel);
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