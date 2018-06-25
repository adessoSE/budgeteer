package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.person.PersonRate;

public class PersonRateModel implements IModel<PersonRate>, IObjectClassAwareModel<PersonRate> {

	private IModel<PersonRate> wrappedModel;

	public PersonRateModel(IModel<PersonRate> wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	public PersonRateModel(PersonRate rate) {
		this.wrappedModel = Model.of(rate);
	}

	@Override
	public Class<PersonRate> getObjectClass() {
		return PersonRate.class;
	}

	@Override
	public PersonRate getObject() {
		return wrappedModel.getObject();
	}

	@Override
	public void setObject(PersonRate object) {
		wrappedModel.setObject(object);
	}

	@Override
	public void detach() {
		wrappedModel.detach();
	}
}
