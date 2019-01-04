package de.adesso.budgeteer.web.pages.budgets.hours;

import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.service.person.PersonService;
import de.adesso.budgeteer.service.record.WorkRecordFilter;
import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.components.burntable.BurnTableWithFilter;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.budgets.BudgetNameModel;
import de.adesso.budgeteer.web.pages.budgets.details.BudgetDetailsPage;
import de.adesso.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

@Mount("budgets/hours/${id}")
public class BudgetHoursPage extends BasePage {

    @Inject
    private PersonService personService;

    public BudgetHoursPage(PageParameters parameters) {
        super(parameters);

        WorkRecordFilter filter = new WorkRecordFilter(BudgeteerSession.get().getProjectId());
        filter.getBudgetList().add(new BudgetBaseData(getParameterId(), ""));
        filter.getPossiblePersons().addAll(personService.loadPeopleBaseDataByBudget(getParameterId()));

        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", filter, this, parameters);
        table.setBudgetFilterEnabled(false);
        add(table);
    }

    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, BudgetsOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(BudgetDetailsPage.class, getPageParameters(), new BudgetNameModel(getParameterId())));
        model.addBreadcrumb(new Breadcrumb(BudgetHoursPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }

}
