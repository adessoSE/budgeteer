package de.adesso.budgeteer.web.pages.person.details.highlights;

import de.adesso.budgeteer.service.person.PersonDetailData;
import de.adesso.budgeteer.service.person.PersonService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PersonHighlightsModel extends LoadableDetachableModel<PersonDetailData> implements IObjectClassAwareModel<PersonDetailData> {

    @SpringBean
    private PersonService service;

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
