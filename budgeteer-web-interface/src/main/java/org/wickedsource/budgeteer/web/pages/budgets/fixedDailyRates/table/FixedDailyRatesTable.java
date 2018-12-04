package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.FixedDailyRatesPage;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.EditFixedDailyRatesPage;

import java.util.List;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class FixedDailyRatesTable extends Panel {
    private PageParameters parameters;

    @SpringBean
    private FixedDailyRateService fixedDailyRateService;

    public FixedDailyRatesTable(String id, IModel<List<FixedDailyRate>> model, PageParameters parameters) {
        super(id, model);
        this.parameters = parameters;
        WebMarkupContainer table = new WebMarkupContainer("ratesTable");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        table.add(createRecordList("recordList", model));
        add(table);
    }

    private ListView<FixedDailyRate> createRecordList(String id, IModel<List<FixedDailyRate>> model) {
        return new ListView<FixedDailyRate>(id, model) {
            @Override
            protected void populateItem(final ListItem<FixedDailyRate> item) {
                item.add(new Label("description", model(from(item.getModelObject()).getDescription())));
                item.add(new MoneyLabel("amount", model(from(item.getModelObject()).getMoneyAmount())));
                item.add(new Label("title", model(from(item.getModelObject()).getName())));
                item.add(new Label("startDate", model(from(item.getModelObject()).getStartDate())));
                item.add(new Label("endDate", model(from(item.getModelObject()).getEndDate())));

                Link editRecordLink = new Link("editRate") {
                    @Override
                    public void onClick() {
                        WebPage page = new EditFixedDailyRatesPage(EditFixedDailyRatesPage.createParameters(
                                item.getModelObject().getId()), FixedDailyRatesPage.class, parameters,
                                false, false);
                        setResponsePage(page);
                    }
                };

                item.add(editRecordLink);

                Link deleteRecordButton = new Link("deleteRate") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DeleteDialog() {
                            @Override
                            protected void onYes() {
                                fixedDailyRateService.deleteFixedDailyRate(item.getModelObject().getId());
                                setResponsePage(FixedDailyRatesPage.class, parameters);
                            }

                            @Override
                            protected void onNo() {
                                setResponsePage(FixedDailyRatesPage.class);
                            }

                            @Override
                            protected String confirmationText() {
                                return FixedDailyRatesTable.this.getString("delete.rate.confirmation");
                            }
                        });
                    }
                };
                item.add(deleteRecordButton);
            }
        };
    }
}