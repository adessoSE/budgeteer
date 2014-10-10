package org.wickedsource.budgeteer.web.usecase.people.edit.component.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.wickedsource.budgeteer.service.people.PersonRate;

public class RatesModel implements IModel<PersonRate>, IObjectClassAwareModel<PersonRate> {

    private IModel<PersonRate> wrappedModel;

    public RatesModel(IModel<PersonRate> wrappedModel) {
        this.wrappedModel = wrappedModel;
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
