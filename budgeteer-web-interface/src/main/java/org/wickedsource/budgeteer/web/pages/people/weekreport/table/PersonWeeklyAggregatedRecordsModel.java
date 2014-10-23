package org.wickedsource.budgeteer.web.pages.people.weekreport.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.record.AggregatedWorkingRecord;
import org.wickedsource.budgeteer.service.record.WorkingRecordService;

import java.util.List;

public class PersonWeeklyAggregatedRecordsModel extends LoadableDetachableModel<List<AggregatedWorkingRecord>> {

    @SpringBean
    private WorkingRecordService service;

    private long personId;

    public PersonWeeklyAggregatedRecordsModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<AggregatedWorkingRecord> load() {
        return service.getWeeklyAggregationForPerson(personId);
    }
}
