package org.wickedsource.budgeteer.web.pages.people.hours;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.people.PersonBaseData;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.burntable.BurnTableWithFilter;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.people.PersonBasePage;
import org.wickedsource.budgeteer.web.pages.people.PersonNameModel;
import org.wickedsource.budgeteer.web.pages.people.details.PersonDetailsPage;
import org.wickedsource.budgeteer.web.pages.people.overview.PeopleOverviewPage;

@Mount("people/hours/${id}")
public class PersonHoursPage extends PersonBasePage {

    public PersonHoursPage(PageParameters parameters) {
        super(parameters);

        RecordFilter filter = new RecordFilter();
        filter.setPerson(new PersonBaseData());
        filter.getPerson().setId(getPersonId());

        BurnTableWithFilter table = new BurnTableWithFilter("burnTable", filter);
        table.setPersonFilterEnabled(false);
        add(table);
    }

    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, PersonDetailsPage.createParameters(getPersonId()), new PersonNameModel(getPersonId())));
        model.addBreadcrumb(new Breadcrumb(PersonHoursPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }

}
