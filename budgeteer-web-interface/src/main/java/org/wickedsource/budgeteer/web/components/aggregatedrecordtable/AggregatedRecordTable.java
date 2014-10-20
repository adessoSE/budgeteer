package org.wickedsource.budgeteer.web.components.aggregatedrecordtable;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;

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
                item.add(new MoneyLabel("budgetBurned", model(from(item.getModel()).getBudgetBurned())));
                item.add(new MoneyLabel("budgetPlanned", model(from(item.getModel()).getBudgetPlanned())));
                Label differenceLabel = new MoneyLabel("difference", model(from(item.getModel()).getDifference())) {
                    @Override
                    protected void onConfigure() {
                        IModel<Money> model = (IModel<Money>) getDefaultModel();
                        if (model.getObject().isPositive()) {
                            add(new AttributeAppender("class", "text-green"));
                        } else if (model.getObject().isNegative()) {
                            add(new AttributeAppender("class", "text-red"));
                        }
                    }
                };
                item.add(differenceLabel);
            }

            @Override
            protected ListItem<AggregatedRecord> newItem(int index, IModel<AggregatedRecord> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<AggregatedRecord>(itemModel, AggregatedRecord.class));
            }
        };
    }
}
