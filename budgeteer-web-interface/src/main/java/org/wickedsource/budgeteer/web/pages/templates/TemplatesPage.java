package org.wickedsource.budgeteer.web.pages.templates;

import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("templates")
public class TemplatesPage extends BasePage {

    public TemplatesPage() {
        long projectId = BudgeteerSession.get().getProjectId();
        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", new WorkRecordFilter(projectId), true);
        add(table);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, TemplatesPage.class);
    }

}
