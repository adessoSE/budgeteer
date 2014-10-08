package org.wickedsource.budgeteer.web.usecase.people.details;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;

@Mount("people/details")
public class PersonDetailsPage extends BasePage {


    public PersonDetailsPage(PageParameters parameters){
        super(parameters);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class, PersonDetailsPage.class);
    }

}
