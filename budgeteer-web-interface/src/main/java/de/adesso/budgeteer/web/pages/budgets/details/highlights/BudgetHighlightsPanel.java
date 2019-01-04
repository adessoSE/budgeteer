package de.adesso.budgeteer.web.pages.budgets.details.highlights;

import de.adesso.budgeteer.service.budget.BudgetDetailData;
import de.adesso.budgeteer.web.components.MarqueeLabel;
import de.adesso.budgeteer.web.components.money.MoneyLabel;
import de.adesso.budgeteer.web.components.nullmodel.NullsafeModel;
import de.adesso.budgeteer.web.components.percent.PercentageLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BudgetHighlightsPanel extends Panel {

    public BudgetHighlightsPanel(String id, IModel<BudgetDetailData> model) {
        super(id, model);
        //add(new MarqueeLabel("name", model(from(model.getObject().getName()))));
        add(new MarqueeLabel("name", model(from(model).getName())));
        add(new MarqueeLabel("contract", nullsafeModel(model(from(model).getContractName()))));
        add(new MarqueeLabel("description", nullsafeModel(model(from(model).getDescription()))));
        add(new MoneyLabel("total", model(from(model).getTotal()), true));
        add(new MoneyLabel("remaining", model(from(model).getRemaining()), true));
        add(new MoneyLabel("spent", model(from(model).getSpent()), true));
        add(new MoneyLabel("limit", model(from(model).getLimit()), true));
        add(new PercentageLabel("progress", model(from(model).getProgress())));
        add(new MoneyLabel("avgDailyRate", model(from(model).getAvgDailyRate()), true));
        add(new Label("lastUpdated", model(from(model).getLastUpdated())));
    }

    private IModel<String> nullsafeModel(IModel<String> wrappedModel) {
        return new NullsafeModel<>(wrappedModel, new StringResourceModel("nullString").getString());
    }

}
