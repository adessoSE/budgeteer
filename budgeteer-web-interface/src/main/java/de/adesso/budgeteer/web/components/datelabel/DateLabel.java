package de.adesso.budgeteer.web.components.datelabel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;

public class DateLabel extends Label {

    public DateLabel(String id, IModel<Date> model) {
        super(id);
        if (model.getObject() == null) {
            setDefaultModel(Model.of(getString("nullString")));
        } else {
            setDefaultModel(model);
        }
    }
}
