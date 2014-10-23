package org.wickedsource.budgeteer.web.pages.people.monthreport.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.record.AggregatedWorkingRecord;
import org.wickedsource.budgeteer.service.record.WorkingRecordService;

import java.util.List;

public class PersonMonthlyAggregatedRecordsModel extends LoadableDetachableModel<List<AggregatedWorkingRecord>> {

    @SpringBean
    private WorkingRecordService service;

    private long personId;

    public PersonMonthlyAggregatedRecordsModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<AggregatedWorkingRecord> load() {
        return service.getMonthlyAggregationForPerson(personId);
    }
}
