package org.wickedsource.budgeteer.web.usecase.people.monthreport;

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
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.Breadcrumb;
import org.wickedsource.budgeteer.web.usecase.base.component.breadcrumb.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.PersonBasePage;
import org.wickedsource.budgeteer.web.usecase.people.details.PersonDetailsPage;
import org.wickedsource.budgeteer.web.usecase.people.details.PersonNameModel;
import org.wickedsource.budgeteer.web.usecase.people.monthreport.component.monthreporttable.PersonMonthlyAggregatedRecordsModel;
import org.wickedsource.budgeteer.web.usecase.people.monthreport.component.targetactualchart.PersonMonthlyAggregationModel;
import org.wickedsource.budgeteer.web.usecase.people.overview.PeopleOverviewPage;

import java.util.List;

@Mount("people/months/${id}")
public class PersonMonthReportPage extends PersonBasePage {

    public PersonMonthReportPage(PageParameters parameters) {
        super(parameters);
        PersonNameModel personNameModel = new PersonNameModel(getPersonId());
        add(new Label("personName", personNameModel));
        add(new Label("personName2", personNameModel));

        IModel<TargetAndActual> model = new PersonMonthlyAggregationModel(getPersonId());
        add(new TargetAndActualChart("targetAndActualChart", model, new BudgeteerChartTheme(), TargetAndActualChartOptions.Mode.MONTHLY));

        IModel<List<AggregatedRecord>> tableModel = new PersonMonthlyAggregatedRecordsModel(getPersonId());
        add(new AggregatedRecordTable("table", tableModel));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, PersonDetailsPage.createParameters(getPersonId()), new PersonNameModel(getPersonId())));
        model.addBreadcrumb(new Breadcrumb(PersonMonthReportPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }
}
