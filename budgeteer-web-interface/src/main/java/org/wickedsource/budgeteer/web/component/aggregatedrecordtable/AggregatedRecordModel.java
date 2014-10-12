package org.wickedsource.budgeteer.web.component.aggregatedrecordtable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;

/**
 * Wrapping model to be used with LazyModel.
 */
public class AggregatedRecordModel implements IObjectClassAwareModel<AggregatedRecord> {

    private IModel<AggregatedRecord> wrappedModel;

    public AggregatedRecordModel(IModel<AggregatedRecord> wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    @Override
    public Class<AggregatedRecord> getObjectClass() {
        return AggregatedRecord.class;
    }

    @Override
    public AggregatedRecord getObject() {
        return wrappedModel.getObject();
    }

    @Override
    public void setObject(AggregatedRecord object) {
        wrappedModel.setObject(object);
    }

    @Override
    public void detach() {
        wrappedModel.detach();
    }
}
