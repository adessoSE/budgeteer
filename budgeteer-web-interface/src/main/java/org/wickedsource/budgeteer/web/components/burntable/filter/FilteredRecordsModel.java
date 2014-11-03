package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.record.WorkRecord;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;

import java.util.List;

public class FilteredRecordsModel extends LoadableDetachableModel<List<WorkRecord>> {

    @SpringBean
    private RecordService service;

    private WorkRecordFilter filter;

    public FilteredRecordsModel(WorkRecordFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
    }


    @Override
    protected List<WorkRecord> load() {
        return service.getFilteredRecords(filter);
    }

    public WorkRecordFilter getFilter() {
        return filter;
    }

    public void setFilter(WorkRecordFilter filter) {
        this.filter = filter;
    }
}
