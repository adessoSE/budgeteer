package org.wickedsource.budgeteer.web.pages.person.details.highlights;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

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
		add(new Label("name", nullsafeModel(model(from(getModel()).getName()))));
		add(new MoneyLabel("avgDailyRate", model(from(getModel()).getAverageDailyRate()), true));
		add(new DateLabel("firstBookedDate", model(from(getModel()).getFirstBookedDate())));
		add(new DateLabel("lastBookedDate", model(from(getModel()).getLastBookedDate())));
		add(new Label("hoursBooked", nullsafeDoubleModel(model(from(getModel()).getHoursBooked()))));
		add(new MoneyLabel("budgetBurned", model(from(getModel()).getBudgetBurned()), true));
	}

	private IModel<String> nullsafeModel(IModel<String> wrappedModel){
		return new NullsafeModel<>(wrappedModel, getString("nullString"));
	}

	private IModel<Double> nullsafeDoubleModel(IModel<Double> wrappedModel){
		return new NullsafeModel<>(wrappedModel, 0d);
	}

}
