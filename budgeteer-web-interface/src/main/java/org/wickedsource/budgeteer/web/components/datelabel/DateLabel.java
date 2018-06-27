package org.wickedsource.budgeteer.web.components.datelabel;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

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
