package org.wickedsource.budgeteer.web.pages.contract.details.differenceTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

import java.util.List;

public class DifferenceTable extends GenericPanel<List<ContractStatisticBean>> {

    public DifferenceTable(String id, IModel<List<ContractStatisticBean>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new ListView<ContractStatisticBean>("row", getModelObject()) {
            @Override
            protected void populateItem(ListItem<ContractStatisticBean> item) {
                WebMarkupContainer month = new WebMarkupContainer("month");
                String monthNumber = item.getModelObject().getMonth() < 10 ? "0"+item.getModelObject().getMonth() : "" + item.getModelObject().getMonth();
                month.add(new Label("monthSort", item.getModelObject().getYear() + "," + monthNumber));
                month.add(new Label("monthName", PropertyLoader.getProperty(BasePage.class, ("monthRenderer.name." + item.getModelObject().getMonth()))));
                item.add(month);
                item.add(new Label("invoiced", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(item.getModelObject().getInvoicedBudget()), BudgeteerSession.get().getSelectedBudgetUnit()))));
                item.add(new Label("spend", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(item.getModelObject().getSpendBudget()), BudgeteerSession.get().getSelectedBudgetUnit()))));
                item.add(new Label("difference", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(item.getModelObject().getDifference()), BudgeteerSession.get().getSelectedBudgetUnit()))));
            }
        });


        //footer row
        long sumInvoiced = 0;
        long sumSpend = 0;
        long sumDifference = 0;
        for(ContractStatisticBean b : getModelObject()){
            sumInvoiced += b.getInvoicedBudget();
            sumSpend += b.getSpendBudget();
            sumDifference += b.getDifference();
        }
        table.add(new Label("sumInvoiced", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(sumInvoiced), BudgeteerSession.get().getSelectedBudgetUnit()))));
        table.add(new Label("sumSpend", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(sumSpend), BudgeteerSession.get().getSelectedBudgetUnit()))));
        table.add(new Label("sumDifference", Model.of(MoneyUtil.toDouble(MoneyUtil.createMoneyFromCents(sumDifference), BudgeteerSession.get().getSelectedBudgetUnit()))));

        add(table);
    }
}
