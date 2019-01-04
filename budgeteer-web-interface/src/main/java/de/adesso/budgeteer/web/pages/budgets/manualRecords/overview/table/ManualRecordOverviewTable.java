package de.adesso.budgeteer.web.pages.budgets.manualRecords.overview.table;

import de.adesso.budgeteer.service.manualRecord.ManualRecord;
import de.adesso.budgeteer.service.manualRecord.ManualRecordService;
import de.adesso.budgeteer.web.components.dataTable.DataTableBehavior;
import de.adesso.budgeteer.web.components.money.MoneyLabel;
import de.adesso.budgeteer.web.pages.base.delete.DeleteDialog;
import de.adesso.budgeteer.web.pages.budgets.manualRecords.add.AddManualRecordPage;
import de.adesso.budgeteer.web.pages.budgets.manualRecords.overview.ManualRecordOverviewPage;
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

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ManualRecordOverviewTable extends Panel {
    private PageParameters parameters;

    @SpringBean
    private ManualRecordService manualRecordService;

    public ManualRecordOverviewTable(String id, IModel<List<ManualRecord>> model, PageParameters parameters) {
        super(id, model);
        this.parameters = parameters;
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));
        table.add(createRecordList("recordList", model));

        add(table);
    }

    private ListView<ManualRecord> createRecordList(String id, IModel<List<ManualRecord>> model) {
        return new ListView<ManualRecord>(id, model) {
            @Override
            protected void populateItem(final ListItem<ManualRecord> item) {
                item.add(new Label("description", model(from(item.getModelObject()).getDescription())));
                item.add(new MoneyLabel("amount", model(from(item.getModelObject()).getMoneyAmount())));
                item.add(new Label("creationDate", model(from(item.getModelObject()).getCreationDate())));
                item.add(new Label("billingDate", model(from(item.getModelObject()).getBillingDate())));

                Link editRecordLink = new Link("editRecord") {
                    @Override
                    public void onClick() {
                        WebPage page = new AddManualRecordPage(AddManualRecordPage.createParameters(
                                item.getModelObject().getId()), ManualRecordOverviewPage.class, parameters, false, false);
                        setResponsePage(page);
                    }
                };
                item.add(editRecordLink);

                Link deleteRecordButton = new Link("deleteRecord") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DeleteDialog() {
                            @Override
                            protected void onYes() {
                                manualRecordService.deleteRecord(item.getModelObject().getId());
                                setResponsePage(ManualRecordOverviewPage.class, parameters);
                            }

                            @Override
                            protected void onNo() {
                                setResponsePage(ManualRecordOverviewPage.class);
                            }

                            @Override
                            protected String confirmationText() {
                                return ManualRecordOverviewTable.this.getString("delete.record.confirmation");
                            }
                        });
                    }
                };

                item.add(deleteRecordButton);
            }
        };
    }
}
