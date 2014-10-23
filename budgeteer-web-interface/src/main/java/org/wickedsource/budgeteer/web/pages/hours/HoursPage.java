package org.wickedsource.budgeteer.web.pages.hours;

import org.wickedsource.budgeteer.service.record.WorkingRecordFilter;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("hours")
public class HoursPage extends BasePage {

    public HoursPage() {
        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", new WorkingRecordFilter());
        add(table);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, HoursPage.class);
    }

}
