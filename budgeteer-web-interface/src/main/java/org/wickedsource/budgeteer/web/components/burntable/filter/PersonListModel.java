package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.person.PersonService;

import java.util.List;

public class PersonListModel extends LoadableDetachableModel<List<PersonBaseData>> {

    @SpringBean
    private PersonService service;

    private long projectId;

    public PersonListModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    protected List<PersonBaseData> load() {
        return service.loadPeopleBaseData(projectId);
    }
}
