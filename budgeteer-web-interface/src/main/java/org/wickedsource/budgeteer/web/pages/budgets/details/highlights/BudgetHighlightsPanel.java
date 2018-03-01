package org.wickedsource.budgeteer.web.pages.budgets.details.highlights;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.nullmodel.NullsafeModel;
import org.wickedsource.budgeteer.web.components.percent.PercentageLabel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BudgetHighlightsPanel extends Panel {

    public BudgetHighlightsPanel(String id, IModel<BudgetDetailData> model) {
        super(id, model);
        add(new Label("name", model(from(model).getName())));
        add(new Label("contract", nullsafeModel(model(from(model).getContractName()))));
        add(new Label("description", nullsafeModel(model(from(model).getDescription()))));
        add(new MoneyLabel("total", model(from(model).getTotal()), true));
        add(new MoneyLabel("remaining", model(from(model).getRemaining()), true));
        add(new PercentageLabel("progress", model(from(model).getProgress())));
        add(new MoneyLabel("avgDailyRate", model(from(model).getAvgDailyRate()), true));
        add(new Label("lastUpdated", model(from(model).getLastUpdated())));
    }

    private IModel<String> nullsafeModel(IModel<String> wrappedModel){
        return new NullsafeModel<>(wrappedModel, getString("nullString"));
    }

}
