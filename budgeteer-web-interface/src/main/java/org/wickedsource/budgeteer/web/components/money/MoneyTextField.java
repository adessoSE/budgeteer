package org.wickedsource.budgeteer.web.components.money;


import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.joda.money.Money;

public class MoneyTextField extends TextField<Money> {

	public MoneyTextField(String id, IModel<Money> model) {
		super(id, model);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Money> IConverter<Money> getConverter(Class<Money> type) {
		MoneyConverter converter = new MoneyConverter();
		return (IConverter<Money>) converter;
	}

	@Override
	protected String[] getInputTypes() {
		return new String[]{"text"};
	}
}
