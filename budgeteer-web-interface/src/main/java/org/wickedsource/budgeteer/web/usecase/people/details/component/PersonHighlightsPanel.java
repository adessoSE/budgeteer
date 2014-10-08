package org.wickedsource.budgeteer.web.usecase.people.details.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.people.PersonDetailData;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class PersonHighlightsPanel extends Panel {

    public PersonHighlightsPanel(String id, IModel<PersonDetailData> model) {
        super(id, model);
        add(new Label("name", model(from(model).getName())));
        add(new Label("avgDailyRate", model(from(model).getAverageDailyRate())));
        add(new Label("firstBookedDate", model(from(model).getFirstBookedDate())));
        add(new Label("lastBookedDate", model(from(model).getLastBookedDate())));
        add(new Label("hoursBooked", model(from(model).getHoursBooked())));
        add(new Label("budgetBurned", model(from(model).getBudgetBurned())));
    }

}
