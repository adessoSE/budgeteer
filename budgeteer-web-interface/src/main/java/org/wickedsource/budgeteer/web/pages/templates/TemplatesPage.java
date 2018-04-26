package org.wickedsource.budgeteer.web.pages.templates;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.components.templatesTable.TemplateListModel;
import org.wickedsource.budgeteer.web.components.templatesTable.TemplatesTable;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.budgets.monthreport.multi.MultiBudgetMonthReportPage;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("template")
public class TemplatesPage extends BasePage {

    public TemplatesPage() {
        long projectId = BudgeteerSession.get().getProjectId();
        TemplatesTable table = new TemplatesTable("templateTable", new TemplateListModel(BudgeteerSession.get().getProjectId()));
        add(table);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, TemplatesPage.class);
    }

}
