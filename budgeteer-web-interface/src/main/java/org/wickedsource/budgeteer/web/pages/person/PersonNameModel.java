package org.wickedsource.budgeteer.web.pages.person;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.person.PersonService;

/**
* A model that loads a person's name from the database.s
*/
public class PersonNameModel extends LoadableDetachableModel<String> {

	@SpringBean
	private PersonService service;

	private long personId;

	public PersonNameModel(long personId) {
		Injector.get().inject(this);
		this.personId = personId;
	}

	@Override
	protected String load() {
		return service.loadPersonDetailData(personId).getName();
	}
}
