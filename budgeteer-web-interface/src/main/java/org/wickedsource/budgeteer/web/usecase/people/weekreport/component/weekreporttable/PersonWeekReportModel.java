package org.wickedsource.budgeteer.web.usecase.people.weekreport.component.weekreporttable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.hours.AggregatedRecord;
import org.wickedsource.budgeteer.service.hours.AggregationService;

import java.util.List;

public class PersonWeekReportModel extends LoadableDetachableModel<List<AggregatedRecord>> {

    @SpringBean
    private AggregationService service;

    private long personId;

    public PersonWeekReportModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<AggregatedRecord> load() {
        return service.getWeeklyAggregationForPerson(personId);
    }
}
