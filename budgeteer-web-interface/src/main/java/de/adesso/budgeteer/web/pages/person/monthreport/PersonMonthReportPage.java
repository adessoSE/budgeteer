package de.adesso.budgeteer.web.pages.person.monthreport;

import de.adesso.budgeteer.service.record.AggregatedRecord;
import de.adesso.budgeteer.service.statistics.TargetAndActual;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.Breadcrumb;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.person.PersonNameModel;
import de.adesso.budgeteer.web.pages.person.details.PersonDetailsPage;
import de.adesso.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import de.adesso.budgeteer.web.components.aggregatedrecordtable.AggregatedRecordTable;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChart;
import de.adesso.budgeteer.web.components.targetactualchart.TargetAndActualChartConfiguration;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import de.adesso.budgeteer.web.pages.person.monthreport.chart.PersonMonthlyAggregationModel;
import de.adesso.budgeteer.web.pages.person.monthreport.table.PersonMonthlyAggregatedRecordsModel;

import java.util.List;

@Mount("people/months/${id}")
public class PersonMonthReportPage extends BasePage {

    public PersonMonthReportPage(PageParameters parameters) {
        super(parameters);
        PersonNameModel personNameModel = new PersonNameModel(getParameterId());
        add(new Label("personName", personNameModel));
        add(new Label("personName2", personNameModel));

        IModel<TargetAndActual> model = new PersonMonthlyAggregationModel(getParameterId());
        add(new TargetAndActualChart("targetAndActualChart", model, TargetAndActualChartConfiguration.Mode.MONTHLY));

        IModel<List<AggregatedRecord>> tableModel = new PersonMonthlyAggregatedRecordsModel(getParameterId());
        add(new AggregatedRecordTable("table", tableModel));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(new Breadcrumb(PersonDetailsPage.class, PersonDetailsPage.createParameters(getParameterId()), new PersonNameModel(getParameterId())));
        model.addBreadcrumb(new Breadcrumb(PersonMonthReportPage.class, getPageParameters(), getString("breadcrumb.title")));
        return model;
    }
}
