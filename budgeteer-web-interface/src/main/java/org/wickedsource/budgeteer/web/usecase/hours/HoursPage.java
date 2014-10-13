package org.wickedsource.budgeteer.web.usecase.hours;

import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.component.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

@Mount("hours")
public class HoursPage extends BasePage {

    public HoursPage() {
        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", new RecordFilter());
        add(table);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, HoursPage.class);
    }

}
