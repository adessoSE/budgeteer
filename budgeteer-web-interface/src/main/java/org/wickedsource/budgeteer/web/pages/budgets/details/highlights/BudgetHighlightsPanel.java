package org.wickedsource.budgeteer.web.pages.budgets.details.highlights;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.nullmodel.NullsafeModel;
import org.wickedsource.budgeteer.web.components.percent.PercentageLabel;

public class BudgetHighlightsPanel extends Panel {

  public BudgetHighlightsPanel(String id, IModel<BudgetDetailData> model) {
    super(id, model);
    add(new Label("name", model.map(BudgetDetailData::getName)));
    add(new Label("contract", nullsafeModel(model.map(BudgetDetailData::getContractName))));
    add(new Label("description", nullsafeModel(model.map(BudgetDetailData::getDescription))));
    add(new MoneyLabel("total", model.map(BudgetDetailData::getTotal), true));
    add(new MoneyLabel("remaining", model.map(BudgetDetailData::getRemaining), true));
    add(new MoneyLabel("spent", model.map(BudgetDetailData::getSpent), true));
    add(new MoneyLabel("limit", model.map(BudgetDetailData::getLimit), true));
    add(new PercentageLabel("progress", model.map(BudgetDetailData::getProgress)));
    add(new MoneyLabel("avgDailyRate", model.map(BudgetDetailData::getAvgDailyRate), true));
    add(new Label("lastUpdated", model.map(BudgetDetailData::getLastUpdated)));
  }

  private IModel<String> nullsafeModel(IModel<String> wrappedModel) {
    return new NullsafeModel<>(wrappedModel, new StringResourceModel("nullString").getString());
  }
}
