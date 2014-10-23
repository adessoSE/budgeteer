package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.imports.api.WorkingRecord;
import org.wickedsource.budgeteer.service.record.WorkingRecordFilter;
import org.wickedsource.budgeteer.service.record.WorkingRecordService;

import java.util.List;

public class FilteredRecordsModel extends LoadableDetachableModel<List<WorkingRecord>> {

    @SpringBean
    private WorkingRecordService service;

    private WorkingRecordFilter filter;

    public FilteredRecordsModel(WorkingRecordFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
    }


    @Override
    protected List<WorkingRecord> load() {
        return service.getFilteredRecords(filter);
    }

    public WorkingRecordFilter getFilter() {
        return filter;
    }

    public void setFilter(WorkingRecordFilter filter) {
        this.filter = filter;
    }
}
