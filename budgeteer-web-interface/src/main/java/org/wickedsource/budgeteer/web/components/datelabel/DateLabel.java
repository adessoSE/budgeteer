package org.wickedsource.budgeteer.web.components.datelabel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.DateUtil;

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

    public DateLabel(String id, IModel<Date> model, boolean formatting) {
        super(id);
        if (model.getObject() == null) {
            setDefaultModel(Model.of(getString("nullString")));
        } else {
            setDefaultModel(formatting ? Model.of(DateUtil.formatDate(model.getObject()))
                    : model);
        }
    }
}
