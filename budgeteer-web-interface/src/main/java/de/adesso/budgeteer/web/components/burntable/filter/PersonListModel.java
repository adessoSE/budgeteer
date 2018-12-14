package de.adesso.budgeteer.web.components.burntable.filter;

import de.adesso.budgeteer.service.person.PersonBaseData;
import de.adesso.budgeteer.service.person.PersonService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
