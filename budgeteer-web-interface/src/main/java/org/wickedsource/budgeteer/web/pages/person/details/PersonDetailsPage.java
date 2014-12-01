package org.wickedsource.budgeteer.web.pages.person.details;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.components.confirm.ConfirmationForm;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.person.PersonBasePage;
import org.wickedsource.budgeteer.web.pages.person.PersonNameModel;
import org.wickedsource.budgeteer.web.pages.person.details.chart.BudgetDistributionChart;
import org.wickedsource.budgeteer.web.pages.person.details.chart.BudgetDistributionChartModel;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsModel;
import org.wickedsource.budgeteer.web.pages.person.details.highlights.PersonHighlightsPanel;
import org.wickedsource.budgeteer.web.pages.person.edit.EditPersonPage;
import org.wickedsource.budgeteer.web.pages.person.hours.PersonHoursPage;
import org.wickedsource.budgeteer.web.pages.person.monthreport.PersonMonthReportPage;
import org.wickedsource.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import org.wickedsource.budgeteer.web.pages.person.weekreport.PersonWeekReportPage;

@Mount("people/details/${id}")
public class PersonDetailsPage extends PersonBasePage {

    @SpringBean
    private PersonService personService;

    public PersonDetailsPage(PageParameters parameters) {
        super(parameters);
        add(new PersonHighlightsPanel("highlightsPanel", new PersonHighlightsModel(getPersonId())));
        add(new BudgetDistributionChart("distributionChart", new BudgetDistributionChartModel(getPersonId()), new BudgeteerChartTheme()));
        add(createEditPersonLink("editPersonLink1"));
        add(createEditPersonLink("editPersonLink2"));
        add(new BookmarkablePageLink<PersonWeekReportPage>("weekReportLink1", PersonWeekReportPage.class, PersonWeekReportPage.createParameters(getPersonId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("weekReportLink2", PersonWeekReportPage.class, PersonWeekReportPage.createParameters(getPersonId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("monthReportLink1", PersonMonthReportPage.class, PersonMonthReportPage.createParameters(getPersonId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("monthReportLink2", PersonMonthReportPage.class, PersonMonthReportPage.createParameters(getPersonId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("hoursLink1", PersonHoursPage.class, PersonHoursPage.createParameters(getPersonId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("hoursLink2", PersonHoursPage.class, PersonHoursPage.createParameters(getPersonId())));

        Form deleteForm = new ConfirmationForm("deleteForm", this, "confirmation.delete") {
            @Override
            public void onSubmit() {
                personService.deletePerson(getPersonId());
                setResponsePage(PeopleOverviewPage.class);
            }
        };
        deleteForm.add(new SubmitLink("deletePersonLink1"));
        deleteForm.add(new SubmitLink("deletePersonLink2"));
        add(deleteForm);
    }

    private Link createEditPersonLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                WebPage page = new EditPersonPage(EditPersonPage.createParameters(getPersonId()), PersonDetailsPage.class, getPageParameters());
                setResponsePage(page);
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, getPageParameters(), new PersonNameModel(getPersonId())));
        return model;
    }

}
