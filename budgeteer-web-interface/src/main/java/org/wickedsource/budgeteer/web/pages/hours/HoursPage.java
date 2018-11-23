package org.wickedsource.budgeteer.web.pages.hours;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.pages.administration.Project;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("hours")
public class HoursPage extends BasePage {

    @SpringBean
    private ProjectService projectService;

    public HoursPage() {
        long projectId = BudgeteerSession.get().getProjectId();
        Project project = projectService.findProjectById(projectId);
        WorkRecordFilter wrf = new WorkRecordFilter(projectId);
        wrf.setDateRange(project.getDateRange());
        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", wrf, true, this, null);
        add(table);
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, HoursPage.class);
    }

}
