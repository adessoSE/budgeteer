package org.wickedsource.budgeteer.web.pages.person.hours;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.person.PersonBasePage;
import org.wickedsource.budgeteer.web.pages.person.PersonNameModel;
import org.wickedsource.budgeteer.web.pages.person.details.PersonDetailsPage;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;

@Mount("people/hours/${id}")
public class PersonHoursPage extends PersonBasePage {

    public PersonHoursPage(PageParameters parameters) {
        super(parameters);

        WorkRecordFilter filter = new WorkRecordFilter(BudgeteerSession.get().getProjectId());
        filter.getPersonList().add(new PersonBaseData(getPersonId()));

        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", filter);
        table.setPersonFilterEnabled(false);
        add(table);
    }

    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, PersonDetailsPage.createParameters(getPersonId()), new PersonNameModel(getPersonId())));
        model.addBreadcrumb(new Breadcrumb(PersonHoursPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }

}
