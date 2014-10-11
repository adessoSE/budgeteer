package org.wickedsource.budgeteer.web.component.aggregatedrecordtable;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.hours.AggregatedRecord;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class AggregatedRecordTable extends Panel {

    public AggregatedRecordTable(String id, IModel<List<AggregatedRecord>> model) {
        super(id, model);
        setRenderBodyOnly(true);
        add(createList("list"));
    }

    @SuppressWarnings("unchecked")
    private ListView<AggregatedRecord> createList(String id) {
        return new ListView<AggregatedRecord>(id, (IModel<List<AggregatedRecord>>) getDefaultModel()) {
            @Override
            protected void populateItem(ListItem<AggregatedRecord> item) {
                item.add(new Label("title", model(from(item.getModel()).getAggregationPeriodTitle())));
                item.add(new Label("startDate", model(from(item.getModel()).getAggregationPeriodStart())));
                item.add(new Label("endDate", model(from(item.getModel()).getAggregationPeriodEnd())));
                item.add(new Label("hours", model(from(item.getModel()).getHours())));
                item.add(new Label("budgetBurned", model(from(item.getModel()).getBudgetBurned())));
                item.add(new Label("budgetPlanned", model(from(item.getModel()).getBudgetPlanned())));
                Label differenceLabel = new Label("difference", model(from(item.getModel()).getDifference())) {
                    @Override
                    protected void onConfigure() {
                        IModel<Double> model = (IModel<Double>) getDefaultModel();
                        if (model.getObject() > 0) {
                            add(new AttributeAppender("class", "text-green"));
                        } else if (model.getObject() < 0) {
                            add(new AttributeAppender("class", "text-red"));
                        }
                    }
                };
                item.add(differenceLabel);
            }

            @Override
            protected ListItem<AggregatedRecord> newItem(int index, IModel<AggregatedRecord> itemModel) {
                return super.newItem(index, new AggregatedRecordModel(itemModel));
            }
        };
    }
}
