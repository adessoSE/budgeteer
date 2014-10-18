package org.wickedsource.budgeteer.web.components.burntable.table;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.wickedsource.budgeteer.service.record.SingleRecord;

public class SingleRecordModel implements IObjectClassAwareModel<SingleRecord> {

    private IModel<SingleRecord> wrappedModel;

    public SingleRecordModel(IModel<SingleRecord> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    @Override
    public Class<SingleRecord> getObjectClass() {
        return SingleRecord.class;
    }

    @Override
    public SingleRecord getObject() {
        return wrappedModel.getObject();
    }

    @Override
    public void setObject(SingleRecord object) {
        wrappedModel.setObject(object);
    }

    @Override
    public void detach() {
        wrappedModel.detach();
    }
}
