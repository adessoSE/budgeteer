package org.wickedsource.budgeteer.web.usecase.budgets.details.component.highlightspanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.web.component.money.MoneyLabel;
import org.wickedsource.budgeteer.web.component.percent.PercentageLabel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BudgetHighlightsPanel extends Panel {

    public BudgetHighlightsPanel(String id, IModel<BudgetDetailData> model) {
        super(id, model);
        add(new Label("name", model(from(model).getName())));
        add(new MoneyLabel("total", model(from(model).getTotal()), true));
        add(new MoneyLabel("remaining", model(from(model).getRemaining()), true));
        add(new PercentageLabel("progress", model(from(model).getProgress())));
        add(new Label("avgDailyRate", model(from(model).getAvgDailyRate())));
        add(new Label("lastUpdated", model(from(model).getLastUpdated())));
    }

}
