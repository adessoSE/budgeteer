package org.wickedsource.budgeteer.web.component.hourstable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonBaseData;

import java.util.List;

public class PersonListModel extends LoadableDetachableModel<List<PersonBaseData>> {

    @SpringBean
    private PeopleService service;

    private long userId;

    public PersonListModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    protected List<PersonBaseData> load() {
        return service.loadPeopleBaseData(userId);
    }
}
