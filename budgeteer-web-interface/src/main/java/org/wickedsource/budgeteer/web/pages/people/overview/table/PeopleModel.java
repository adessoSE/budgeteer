package org.wickedsource.budgeteer.web.pages.people.overview.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonBaseData;

import java.util.List;

public class PeopleModel extends LoadableDetachableModel<List<PersonBaseData>> {

    @SpringBean
    private PeopleService service;

    private long userId;

    public PeopleModel(long userId) {
        this.userId = userId;
        Injector.get().inject(this);
    }

    @Override
    protected List<PersonBaseData> load() {
        return service.loadPeopleBaseData(this.userId);
    }
}
