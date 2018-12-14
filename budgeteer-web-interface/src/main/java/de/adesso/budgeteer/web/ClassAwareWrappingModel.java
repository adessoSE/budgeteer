package de.adesso.budgeteer.web;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;

public class ClassAwareWrappingModel<T> implements IObjectClassAwareModel<T> {

    private IModel<T> wrappedModel;

    private Class<T> clazz;

    public ClassAwareWrappingModel(IModel<T> wrappedModel, Class<T> clazz) {
        this.wrappedModel = wrappedModel;
        this.clazz = clazz;
    }

    @Override
    public Class<T> getObjectClass() {
        return clazz;
    }

    @Override
    public T getObject() {
        return wrappedModel.getObject();
    }

    @Override
    public void setObject(T object) {
        wrappedModel.setObject(object);
    }

    @Override
    public void detach() {
        wrappedModel.detach();
    }
}
