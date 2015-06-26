package org.wickedsource.budgeteer.web.pages.contract.details.belongingObject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ContractBelongingObjectsPanel extends Panel {

    public ContractBelongingObjectsPanel(String id, IModel<ContractBaseData> model) {
        super(id, model);
        WebMarkupContainer table = new WebMarkupContainer("belongingBudgetTable");
        table.add(new ListView<BudgetBaseData>("belongingBudgets", model(from(model.getObject()).getBelongingBudgets())) {

            @Override
            protected void populateItem(ListItem<BudgetBaseData> item) {
                BookmarkablePageLink link = new BookmarkablePageLink("budgetLink", BudgetDetailsPage.class, BasePage.createParameters(item.getModelObject().getId()));
                link.setBody(Model.of(item.getModelObject().getName()));
                item.add(link);
            }
        });
        add(table);
    }
}
