package org.wickedsource.budgeteer.web.usecase.people.weekreport;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.component.aggregatedrecordtable.AggregatedRecordTable;
import org.wickedsource.budgeteer.web.component.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.component.targetactualchart.TargetAndActualChartOptions;
import org.wickedsource.budgeteer.web.usecase.base.BasePage;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.Breadcrumb;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.details.PersonDetailsPage;
import org.wickedsource.budgeteer.web.usecase.people.details.PersonNameModel;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;
import org.wickedsource.budgeteer.web.usecase.people.weekreport.component.targetactualchart.PersonWeeklyAggregationModel;
import org.wickedsource.budgeteer.web.usecase.people.weekreport.component.weekreporttable.PersonWeeklyAggregatedRecordsModel;

import java.util.List;

@Mount("people/weeks/${id}")
public class PersonWeekReportPage extends BasePage {

    public PersonWeekReportPage(PageParameters parameters) {
        super(parameters);
        PersonNameModel personNameModel = new PersonNameModel(getPersonId());
        add(new Label("personName", personNameModel));
        add(new Label("personName2", personNameModel));

        IModel<TargetAndActual> model = new PersonWeeklyAggregationModel(getPersonId());
        add(new TargetAndActualChart("targetAndActualChart", model, new BudgeteerChartTheme(), TargetAndActualChartOptions.Mode.WEEKLY));

        IModel<List<AggregatedRecord>> tableModel = new PersonWeeklyAggregatedRecordsModel(getPersonId());
        add(new AggregatedRecordTable("table", tableModel));
    }

    /**
     * Creates a valid PageParameters object to pass into the constructor of this page class.
     *
     * @param personId id of the person whose details to display.
     * @return a valid PageParameters object.
     */
    public static PageParameters createParameters(long personId) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", personId);
        return parameters;
    }

    private long getPersonId() {
        return getPageParameters().get("id").toLong();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, PersonDetailsPage.createParameters(getPersonId()), new PersonNameModel(getPersonId())));
        model.addBreadcrumb(new Breadcrumb(PersonWeekReportPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }
}
