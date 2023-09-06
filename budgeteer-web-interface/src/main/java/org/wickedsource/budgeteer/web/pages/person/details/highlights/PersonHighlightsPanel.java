package org.wickedsource.budgeteer.web.pages.person.details.highlights;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.web.components.datelabel.DateLabel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.nullmodel.NullsafeModel;

public class PersonHighlightsPanel extends GenericPanel<PersonDetailData> {

  public PersonHighlightsPanel(String id, IModel<PersonDetailData> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(new Label("name", nullsafeModel(getModel().map(PersonDetailData::getName))));
    add(
        new MoneyLabel(
            "avgDailyRate", getModel().map(PersonDetailData::getAverageDailyRate), true));
    add(new DateLabel("firstBookedDate", getModel().map(PersonDetailData::getFirstBookedDate)));
    add(new DateLabel("lastBookedDate", getModel().map(PersonDetailData::getLastBookedDate)));
    add(
        new Label(
            "hoursBooked", nullsafeDoubleModel(getModel().map(PersonDetailData::getHoursBooked))));
    add(
        new MoneyLabel(
            "budgetBurned_net", getModel().map(PersonDetailData::getBudgetBurned), true));
  }

  private IModel<String> nullsafeModel(IModel<String> wrappedModel) {
    return new NullsafeModel<>(wrappedModel, getString("nullString"));
  }

  private IModel<Double> nullsafeDoubleModel(IModel<Double> wrappedModel) {
    return new NullsafeModel<>(wrappedModel, 0d);
  }
}
