package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.tax.TaxBudgetUnitMoneyModel;


import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ManualRecordOverviewTable extends Panel {
    @SpringBean
    private ContractService contractService;

    @SpringBean
    private BudgetService budgetService;

    public ManualRecordOverviewTable(String id, IModel<List<ManualRecord>> model) {
        super(id, model);
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
                item.add(new Label("date", model(from(item.getModelObject()).getDate())));
            }
        };
    }
}
