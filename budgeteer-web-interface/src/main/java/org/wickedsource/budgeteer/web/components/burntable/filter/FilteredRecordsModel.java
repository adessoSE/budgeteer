package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.record.SingleRecord;

import java.util.List;

public class FilteredRecordsModel extends LoadableDetachableModel<List<SingleRecord>> {

    @SpringBean
    private RecordService service;

    private RecordFilter filter;

    public FilteredRecordsModel(RecordFilter filter) {
        Injector.get().inject(this);
        this.filter = filter;
    }


    @Override
    protected List<SingleRecord> load() {
        return service.getFilteredRecords(filter);
    }

    public RecordFilter getFilter() {
        return filter;
    }

    public void setFilter(RecordFilter filter) {
        this.filter = filter;
    }
}
