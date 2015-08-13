package org.wickedsource.budgeteer.web.pages.budgets.overview.table;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.table.progressbar.ProgressBar;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BudgetOverviewTable extends Panel {

    public BudgetOverviewTable(String id, FilteredBudgetModel model) {
        super(id, model);
        WebMarkupContainer table = new WebMarkupContainer("table");
        table.add(new DataTableBehavior(DataTableBehavior.getRecommendedOptions()));


        table.add(createBudgetList("budgetList", model));


        IModel<BudgetDetailData> totalModel = new TotalBudgetDetailsModel(model);
        table.add(new Label("totalLastUpdated", model(from(totalModel).getLastUpdated())));
        table.add(new MoneyLabel("totalAmount", new BudgetUnitMoneyModel(model(from(totalModel).getTotal()))));
        table.add(new MoneyLabel("totalSpent", new BudgetUnitMoneyModel(model(from(totalModel).getSpent()))));
        table.add(new MoneyLabel("totalRemaining", new BudgetUnitMoneyModel(model(from(totalModel).getRemaining()))));
        table.add(new MoneyLabel("totalUnplanned", new BudgetUnitMoneyModel(model(from(totalModel).getUnplanned()))));
        table.add(new ProgressBar("totalProgressBar", model(from(totalModel).getProgressInPercent())));

        add(table);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        if (event.getPayload() instanceof BudgetTagFilter) {
            BudgetTagFilter filter = (BudgetTagFilter) event.getPayload();
            FilteredBudgetModel model = (FilteredBudgetModel) getDefaultModel();
            model.setFilter(model(from(filter)));
        }
    }

    private ListView<BudgetDetailData> createBudgetList(String id, IModel<List<BudgetDetailData>> model) {
        return new ListView<BudgetDetailData>(id, model) {
            @Override
            protected void populateItem(final ListItem<BudgetDetailData> item) {
                BookmarkablePageLink link = new BookmarkablePageLink("detailLink", BudgetDetailsPage.class, BudgetDetailsPage.createParameters(item.getModelObject().getId()));
                Label linkTitle = new Label("detailLinkTitle", model(from(item.getModel()).getName()));
                link.add(linkTitle);
                item.add(link);
                item.add(new Label("lastUpdated", model(from(item.getModel()).getLastUpdated())));
                item.add(new Label("contract", model(from(item.getModel()).getContractName())));
                item.add(new MoneyLabel("amount", new BudgetUnitMoneyModel(model(from(item.getModel()).getTotal()))));
                item.add(new MoneyLabel("spent", new BudgetUnitMoneyModel(model(from(item.getModel()).getSpent()))));
                item.add(new MoneyLabel("remaining", new BudgetUnitMoneyModel(model(from(item.getModel()).getRemaining()))));
                item.add(new MoneyLabel("unplanned", new BudgetUnitMoneyModel(model(from(item.getModel()).getUnplanned()))));
                item.add(new ProgressBar("progressBar", model(from(item.getModel()).getProgressInPercent())));
                Link editPersonLink = new Link("editPage") {
                    @Override
                    public void onClick() {
                        WebPage page = new EditBudgetPage(EditBudgetPage.createParameters(item.getModelObject().getId()), BudgetsOverviewPage.class,null);
                        setResponsePage(page);
                    }
                };
                item.add(editPersonLink);
            }

            @Override
            protected ListItem<BudgetDetailData> newItem(int index, IModel<BudgetDetailData> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<BudgetDetailData>(itemModel, BudgetDetailData.class));
            }
        };
    }
}
