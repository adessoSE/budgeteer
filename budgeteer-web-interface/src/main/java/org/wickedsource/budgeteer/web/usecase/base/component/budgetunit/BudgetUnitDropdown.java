package org.wickedsource.budgeteer.web.usecase.base.component.budgetunit;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.settings.BudgetUnit;
import org.wickedsource.budgeteer.service.settings.SettingsService;
import org.wickedsource.budgeteer.web.fontawesome.FontAwesomeIcon;
import org.wickedsource.budgeteer.web.fontawesome.FontAwesomeIconType;
import org.wickedsource.budgeteer.web.usecase.base.BudgeteerSession;

public class BudgetUnitDropdown extends Panel {

    @SpringBean
    private SettingsService settingsService;

    public BudgetUnitDropdown(String id, IModel<?> model) {
        super(id, model);
        Injector.get().inject(this);
        add(createBudgetList("unitList"));
    }

    private ListView<BudgetUnit> createBudgetList(String wicketId) {
        return new ListView<BudgetUnit>(wicketId, getModel()) {
            @Override
            protected void populateItem(ListItem<BudgetUnit> item) {
                Link<BudgetUnit> link = new Link<BudgetUnit>("link", item.getModel()) {
                    @Override
                    public void onClick() {
                        settingsService.activateBudgetUnit(BudgeteerSession.get().getLoggedInUserId(), getModelObject());
                    }
                };
                item.add(link);

                if (item.getModelObject().isActive()) {
                    link.add(new FontAwesomeIcon("checkboxIcon", FontAwesomeIconType.CHECK_SQUARE_O));
                } else {
                    link.add(new FontAwesomeIcon("checkboxIcon", FontAwesomeIconType.SQUARE_O));
                }

                Label unitTitle = new Label("unitTitle", item.getModelObject().getUnitTitle());
                unitTitle.setRenderBodyOnly(true);
                link.add(unitTitle);
            }
        };
    }

    private BudgetUnitModel getModel() {
        return (BudgetUnitModel) getDefaultModel();
    }


}
