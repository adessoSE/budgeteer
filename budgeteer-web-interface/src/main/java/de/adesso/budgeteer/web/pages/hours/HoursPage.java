package de.adesso.budgeteer.web.pages.hours;

import de.adesso.budgeteer.service.project.ProjectService;
import de.adesso.budgeteer.service.record.WorkRecordFilter;
import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.components.burntable.BurnTableWithFilter;
import de.adesso.budgeteer.web.pages.administration.Project;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
