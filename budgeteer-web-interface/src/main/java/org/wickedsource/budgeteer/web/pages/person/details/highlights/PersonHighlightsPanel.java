package org.wickedsource.budgeteer.web.pages.person.details.highlights;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.person.PersonDetailData;
import org.wickedsource.budgeteer.web.components.datelabel.DateLabel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;
import org.wickedsource.budgeteer.web.components.nullmodel.NullsafeModel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class PersonHighlightsPanel extends Panel {

    public PersonHighlightsPanel(String id, IModel<PersonDetailData> model) {
        super(id, model);
        add(new Label("name", nullsafeModel(model(from(model).getName()))));
        add(new MoneyLabel("avgDailyRate", model(from(model).getAverageDailyRate()), true));
        add(new DateLabel("firstBookedDate", model(from(model).getFirstBookedDate())));
        add(new DateLabel("lastBookedDate", model(from(model).getLastBookedDate())));
        add(new Label("hoursBooked", nullsafeDoubleModel(model(from(model).getHoursBooked()))));
        add(new MoneyLabel("budgetBurned", model(from(model).getBudgetBurned()), true));
    }

    private IModel<String> nullsafeModel(IModel<String> wrappedModel){
        return new NullsafeModel<String>(wrappedModel, getString("nullString"));
    }

    private IModel<Double> nullsafeDoubleModel(IModel<Double> wrappedModel){
        return new NullsafeModel<Double>(wrappedModel, 0d);
    }

}
