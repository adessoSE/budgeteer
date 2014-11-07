package org.wickedsource.budgeteer.web.pages.person.monthreport.chart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

public class PersonMonthlyAggregationModel extends LoadableDetachableModel<TargetAndActual> implements IObjectClassAwareModel<TargetAndActual> {

    @SpringBean
    private StatisticsService service;

    private long personId;

    public PersonMonthlyAggregationModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected TargetAndActual load() {
        return service.getMonthStatsForPerson(personId, 12);
    }

    @Override
    public Class<TargetAndActual> getObjectClass() {
        return TargetAndActual.class;
    }
}
