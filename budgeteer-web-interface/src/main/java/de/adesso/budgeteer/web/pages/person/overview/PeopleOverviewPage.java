package de.adesso.budgeteer.web.pages.person.overview;

import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import de.adesso.budgeteer.web.pages.person.overview.table.PeopleModel;
import de.adesso.budgeteer.web.pages.person.overview.table.PeopleOverviewTable;

@Mount({"people"})
public class PeopleOverviewPage extends BasePage {

    public PeopleOverviewPage() {
        PeopleModel model = new PeopleModel(BudgeteerSession.get().getProjectId());
        PeopleOverviewTable table = new PeopleOverviewTable("peopleOverviewTable", model);
        add(table);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
    }

}
