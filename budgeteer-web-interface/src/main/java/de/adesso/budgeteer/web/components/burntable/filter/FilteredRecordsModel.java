package de.adesso.budgeteer.web.components.burntable.filter;

import de.adesso.budgeteer.service.record.RecordService;
import de.adesso.budgeteer.service.record.WorkRecord;
import de.adesso.budgeteer.service.record.WorkRecordFilter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
