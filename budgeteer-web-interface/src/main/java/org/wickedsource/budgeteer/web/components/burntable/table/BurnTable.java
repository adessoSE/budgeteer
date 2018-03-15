package org.wickedsource.budgeteer.web.components.burntable.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.record.WorkRecord;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilteredRecordsModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.dataTable.editableMoneyField.EditableMoneyField;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;

import javax.inject.Inject;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BurnTable extends Panel {

    private CustomFeedbackPanel feedbackPanel;
    private boolean dailyRateIsEditable;
    private ListView<WorkRecord> rows;

    @Inject
    private RecordService recordService;

    public BurnTable(String id, FilteredRecordsModel model){
        this(id, model, false);
    }

    public BurnTable(String id, FilteredRecordsModel model, boolean dailyRateIsEditable) {
        super(id, model);
        this.dailyRateIsEditable = dailyRateIsEditable;

        feedbackPanel = new CustomFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        rows = createList("recordList", model, table);
        table.add(rows);

        add(table);
        add(new MoneyLabel("total", new BudgetUnitMoneyModel(new TotalBudgetModel(model),1.0)));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        Object payload = event.getPayload();
        if (payload instanceof WorkRecordFilter) {
            WorkRecordFilter filter = (WorkRecordFilter) payload;
            FilteredRecordsModel model = (FilteredRecordsModel) getDefaultModel();
            model.setFilter(filter);
        }
    }

    private ListView<WorkRecord> createList(String id, final IModel<List<WorkRecord>> model, final WebMarkupContainer table) {
        return new ListView<WorkRecord>(id, model) {
            @Override
            protected void populateItem(final ListItem<WorkRecord> item) {
                item.setOutputMarkupId(true);
                item.add(new Label("budget", model(from(item.getModel()).getBudgetName())));
                item.add(new Label("person", model(from(item.getModel()).getPersonName()) ));
                if(dailyRateIsEditable) {
                    final EditableMoneyField editableMoneyField = new EditableMoneyField("dailyRate", table, model(from(item.getModelObject()).getDailyRate())) {
                        @Override
                        protected void save(AjaxRequestTarget target, Form<Money> form) {
                            item.getModelObject().setEditedManually(true);
                            item.getModelObject().setDailyRate(form.getModelObject());
                            recordService.saveDailyRateForWorkRecord(item.getModelObject());
                            target.add(item);
                        }

                        @Override
                        protected void cancel(AjaxRequestTarget target, Form<Money> form) {
                            item.getModelObject().setEditedManually(item.getModelObject().isEditedManually());
                            target.add(item);
                        }

                        @Override
                        protected void convertError(AjaxRequestTarget target) {
                            target.add(feedbackPanel);
                        }
                    };
                    item.add(editableMoneyField);
                } else {
                    item.add(new Label("dailyRate", model(from(item.getModel()).getDailyRate())));
                }
                item.add(new Label("edited"){
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().isEditedManually();
                    }
                });
                item.add(new Label("date", model(from(item.getModel()).getDate())));
                item.add(new Label("hours", model(from(item.getModel()).getHours())));
                item.add(new MoneyLabel("burnedBudget", new BudgetUnitMoneyModel(model(from(item.getModel()).getBudgetBurned()),1.0)));
            }

            @Override
            protected ListItem<WorkRecord> newItem(int index, IModel<WorkRecord> itemModel) {
                // wrap model to work with LazyModel
                return super.newItem(index, new ClassAwareWrappingModel<WorkRecord>(itemModel, WorkRecord.class));
            }
        };
    }

    public ListView<WorkRecord> getRows() {
        return rows;
    }
}
