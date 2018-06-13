package org.wickedsource.budgeteer.web.pages.person.weekreport.table;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.record.RecordService;

public class PersonWeeklyAggregatedRecordsModel
		extends LoadableDetachableModel<List<AggregatedRecord>> {

	@SpringBean private RecordService service;

	private long personId;

	public PersonWeeklyAggregatedRecordsModel(long personId) {
		Injector.get().inject(this);
		this.personId = personId;
	}

	@Override
	protected List<AggregatedRecord> load() {
		return service.getWeeklyAggregationForPerson(personId);
	}
}
