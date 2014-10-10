package org.wickedsource.budgeteer.web.usecase.people.details.component.highlightspanel;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonDetailData;

public class PersonHighlightsModel extends LoadableDetachableModel<PersonDetailData> implements IObjectClassAwareModel<PersonDetailData> {

    @SpringBean
    private PeopleService service;

    private long personId;

    public PersonHighlightsModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected PersonDetailData load() {
        return service.loadPersonDetailData(personId);
    }

    @Override
    public Class getObjectClass() {
        return PersonDetailData.class;
    }
}
