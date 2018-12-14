package de.adesso.budgeteer.web.pages.base.basepage.budgetunitchoice;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import de.adesso.budgeteer.web.components.fontawesome.FontAwesomeIcon;
import de.adesso.budgeteer.web.components.fontawesome.FontAwesomeIconType;

import java.util.List;

public class BudgetUnitChoice extends Panel {

    public BudgetUnitChoice(String id, IModel<List<Double>> model) {
        super(id, model);
        Injector.get().inject(this);
        add(createBudgetList("unitList"));
    }

    private ListView<Double> createBudgetList(String wicketId) {
        return new ListView<Double>(wicketId, getModel()) {
            @Override
            protected void populateItem(final ListItem<Double> item) {
                final BudgeteerSession session = BudgeteerSession.get();
                Link<Double> link = new Link<Double>("link", item.getModel()) {
                    @Override
                    public void onClick() {
                        session.setSelectedBudgetUnit(item.getModelObject());
                        setResponsePage(this.getPage().getPageClass(), this.getPage().getPageParameters());
                    }
                };
                item.add(link);

                if (session.getSelectedBudgetUnit() == null || item.getModelObject().equals(session.getSelectedBudgetUnit())) {
                    link.add(new FontAwesomeIcon("checkboxIcon", FontAwesomeIconType.CHECK_SQUARE_O));
                } else {
                    link.add(new FontAwesomeIcon("checkboxIcon", FontAwesomeIconType.SQUARE_O));
                }

                String unitname = "";
                if (link.getModelObject().equals(1d)) {
                    unitname = getString("unitname.money");
                } else {
                    unitname = String.format(getString("unitname.manday"), MoneyUtil.createMoney(item.getModelObject()));
                }

                Label unitTitle = new Label("unitTitle", unitname);
                unitTitle.setRenderBodyOnly(true);
                link.add(unitTitle);
            }
        };
    }

    private BudgetUnitModel getModel() {
        return (BudgetUnitModel) getDefaultModel();
    }


}
