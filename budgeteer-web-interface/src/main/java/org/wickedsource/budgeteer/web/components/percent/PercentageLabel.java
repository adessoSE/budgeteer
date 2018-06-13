package org.wickedsource.budgeteer.web.components.percent;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class PercentageLabel extends Label {

	public PercentageLabel(String id, IModel<Double> model) {
		super(id, model);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Double> IConverter<Double> getConverter(Class<Double> type) {
		return (IConverter<Double>) new PercentageConverter();
	}
}
