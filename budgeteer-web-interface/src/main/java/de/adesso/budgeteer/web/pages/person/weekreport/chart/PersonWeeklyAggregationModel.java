package de.adesso.budgeteer.web.pages.person.weekreport.chart;

import de.adesso.budgeteer.service.statistics.StatisticsService;
import de.adesso.budgeteer.service.statistics.TargetAndActual;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PersonWeeklyAggregationModel extends LoadableDetachableModel<TargetAndActual> implements IObjectClassAwareModel<TargetAndActual> {

    @SpringBean
    private StatisticsService service;

    private long personId;

    public PersonWeeklyAggregationModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected TargetAndActual load() {
        return service.getWeekStatsForPerson(personId, 12);
    }

    @Override
    public Class<TargetAndActual> getObjectClass() {
        return TargetAndActual.class;
    }
}
