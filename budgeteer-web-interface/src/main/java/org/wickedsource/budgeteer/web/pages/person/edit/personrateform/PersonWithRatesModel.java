package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.person.PersonWithRates;

public class PersonWithRatesModel implements IModel<PersonWithRates>, IObjectClassAwareModel<PersonWithRates> {

	private IModel<PersonWithRates> model;

	public PersonWithRatesModel(PersonWithRates person) {
		this.model = Model.of(person);
	}

	public PersonWithRatesModel(IModel<PersonWithRates> model) {
		this.model = model;
	}

	@Override
	public Class<PersonWithRates> getObjectClass() {
		return PersonWithRates.class;
	}

	@Override
	public PersonWithRates getObject() {
		return model.getObject();
	}

	@Override
	public void setObject(PersonWithRates object) {
		this.model.setObject(object);
	}

	@Override
	public void detach() {

	}
}
