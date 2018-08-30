package de.adesso.budgeteer.web.components.nullmodel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

public class NullsafeModel<T extends Serializable> implements IModel<T> {

    private IModel<T> wrappedModel;

    private T nullValue;

    public NullsafeModel(IModel<T> wrappedModel, T nullValue) {
        this.wrappedModel = wrappedModel;
        this.nullValue = nullValue;
    }

    public NullsafeModel(T value, T nullValue){
        this.wrappedModel = new Model<T>(value);
        this.nullValue = nullValue;
    }

    @Override
    public T getObject() {
        T value = wrappedModel.getObject();
        if (value == null) {
            return nullValue;
        } else {
            return value;
        }
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
