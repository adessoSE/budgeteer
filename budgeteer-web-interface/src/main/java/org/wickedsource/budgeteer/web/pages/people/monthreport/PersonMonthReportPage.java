package org.wickedsource.budgeteer.web.pages.people.monthreport;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.AggregatedWorkingRecord;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.charts.BudgeteerChartTheme;
import org.wickedsource.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import org.wickedsource.budgeteer.web.components.targetactualchart.TargetAndActualChartOptions;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.people.PersonBasePage;
import org.wickedsource.budgeteer.web.pages.people.PersonNameModel;
import org.wickedsource.budgeteer.web.pages.people.details.PersonDetailsPage;
import org.wickedsource.budgeteer.web.pages.people.monthreport.chart.PersonMonthlyAggregationModel;
import org.wickedsource.budgeteer.web.pages.people.monthreport.table.PersonMonthlyAggregatedRecordsModel;
import org.wickedsource.budgeteer.web.pages.people.overview.PeopleOverviewPage;

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

        IModel<List<AggregatedWorkingRecord>> tableModel = new PersonMonthlyAggregatedRecordsModel(getPersonId());
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
